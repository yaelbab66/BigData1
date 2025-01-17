package mostFreqAppDays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.fs.FileSystem;

public class MostFreqAppDays extends Configured implements Tool {

    public int run(String[] args) throws Exception {
        int numberOfReducers = Integer.parseInt(args[0]);
        Path inputDir = new Path(args[1]);
        Path output = new Path(args[2]);
        String wordLenLimit = args[3];

        Configuration conf = getConf();
        conf.setInt("wordLenLimit", Integer.parseInt(wordLenLimit));

        Job job = Job.getInstance(conf);

        job.setJobName("MostFreqAppDays");

        // Set input and output paths
        FileInputFormat.addInputPath(job, inputDir);
        FileOutputFormat.setOutputPath(job, output);

        // Set input and output formats
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // Set jar class
        job.setJarByClass(MostFreqAppDays.class);

        // Mapper setup
        job.setMapperClass(MostFreqAppDaysMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // Reducer setup
        job.setReducerClass(MostFreqAppDaysReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class); 

        job.setNumReduceTasks(numberOfReducers);

        // Check and delete the output directory if it already exists
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(output)) {
            fs.delete(output, true);
        }

        // Wait for job completion
        if (job.waitForCompletion(true)) {
            return 0;
        }
        return 1;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new MostFreqAppDays(), args);
        System.exit(res);
    }
}
