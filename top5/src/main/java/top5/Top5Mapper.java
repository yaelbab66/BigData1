package top5;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Top5Mapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	@Override
	protected void map(LongWritable kay, Text value, Context context) throws IOException, InterruptedException{
		
		String[] parts = value.toString().split( ",");

	    String groupKey = parts[1];
	    if (! groupKey.equals("App")) {
	        int val = Integer.parseInt(parts[2]);

	        
	        context.write(new Text(groupKey), new IntWritable(val));
	    }
		
	}
}

	
