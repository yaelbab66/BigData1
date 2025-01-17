package top5;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


/*
 * Implements the - Driver class
 * */
public class Top5 extends Configured implements Tool {

    public int run(String[] args) throws Exception {
        int numberOfReducers = Integer.parseInt(args[0]);
        Path inputDir = new Path(args[1]);
        Path output = new Path(args[2]);
        String wordLenLimit = args[3];

        Configuration conf = getConf();
        conf.setInt("wordLenLimit", Integer.parseInt(wordLenLimit));

        Job job = Job.getInstance(conf);

        job.setJobName("Top5");

        FileInputFormat.addInputPath(job, inputDir);
        FileOutputFormat.setOutputPath(job, output);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputValueClass(TextOutputFormat.class);

        job.setJarByClass(Top5.class);

        // Init mapper
        job.setMapperClass(Top5Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // Init reducer
        job.setReducerClass(Top5Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        job.setNumReduceTasks(numberOfReducers);

        if (job.waitForCompletion(true))
            return 0;

        return 1;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new Top5(), args);
        System.exit(res);
    }
}
