package notvsUse;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class NotvsUseMapper extends Mapper<LongWritable, Text, Text, IntArrayWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split(","); 
        String keyText = fields[1]; // First column is the key
        if (! keyText.equals("App")) {
        	int nValue = Integer.parseInt(fields[3]); // Second column
        	int uValue = Integer.parseInt(fields[2]); // Third column
        

	        // Create IntArrayWritable for the two values
	        IntWritable[] outputArray = new IntWritable[]{
	            new IntWritable(nValue),
	            new IntWritable(uValue)
	        };

	        context.write(new Text(keyText), new IntArrayWritable(outputArray));
        }
    }
}
