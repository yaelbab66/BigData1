package notvsUse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;


/*
 * Implements the - Driver class
 * */
public class NotvsUse extends Configured implements Tool {

    public int run(String[] args) throws Exception {
        int numberOfReducers = Integer.parseInt(args[0]);
        Path inputDir = new Path(args[1]);
        Path output = new Path(args[2]);
        String wordLenLimit = args[3];

        Configuration conf = getConf();
        conf.setInt("wordLenLimit", Integer.parseInt(wordLenLimit));

        Job job = Job.getInstance(conf);

        job.setJobName("NotvsAn");

        
        
        
        FileInputFormat.addInputPath(job, inputDir);
        FileOutputFormat.setOutputPath(job, output);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputValueClass(TextOutputFormat.class);

        job.setJarByClass(NotvsUse.class);

        // Init mapper
        job.setMapperClass(NotvsUseMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntArrayWritable.class);

        // Init reducer
        job.setReducerClass(NotvsUseReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        job.setNumReduceTasks(numberOfReducers);

        if (!job.waitForCompletion(true))
            return 1;
        System.out.print("output: " + output);
        generateReport(output.toString() + "/part-r-00000");
       
        return 0;
    }
    
    

    private void generateReport(String outputFilePath) {
        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            Path file = new Path(outputFilePath);
            if (!fs.exists(file)) {
                System.err.println("Output file does not exist: " + outputFilePath);
                return;
            }

            // Read the file from HDFS
            FSDataInputStream inputStream = fs.open(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            List<AppData> appDataList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String appName = parts[0].trim();
                    String[] values = parts[1].trim().split(" ");
                    if (values.length == 2) {
                        int totalNotifications = Integer.parseInt(values[0]);
                        int totalUsage = Integer.parseInt(values[1]);
                        appDataList.add(new AppData(appName, totalUsage, totalNotifications));
                    }
                }
            }

            // Close the reader
            reader.close();

            // Sort and print
            appDataList.sort((a, b) -> Integer.compare(b.getTotalUsage(), a.getTotalUsage()));
            System.out.printf("%-12s %-25s %-20s%n", "App", "Total Usage (minutes)", "Total Notifications");
            System.out.println("---------------------------------------------------------------");
            for (AppData app : appDataList) {
                System.out.printf("%-12s %-25d %-20d%n", app.getName(), app.getTotalUsage(), app.getTotalNotifications());
            }
        } catch (IOException e) {
            System.err.println("Error reading output file or generating report: " + e.getMessage());
        }
    }

    

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new NotvsUse(), args);
        System.exit(res);
    }
}

