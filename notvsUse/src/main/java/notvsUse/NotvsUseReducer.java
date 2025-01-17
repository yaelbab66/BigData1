package notvsUse;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


public class NotvsUseReducer extends Reducer<Text, IntArrayWritable, Text, Text> {
    
    @Override
    protected void reduce(Text key, Iterable<IntArrayWritable> values, Context context) throws IOException, InterruptedException {
    	//Notifications 
    	int sumN = 0;
    	//Usage (minutes)
         int sumU = 0;

         // Iterate through IntArrayWritable values
         for (IntArrayWritable value : values) {
             Writable[] writables = value.get();

             if (writables.length == 2) {
                 int nValue = ((IntWritable) writables[0]).get();
                 int uValue = ((IntWritable) writables[1]).get();

                 sumN += nValue;
                 sumU += uValue;
             }
         }

         // Format the output as "sumN sumU"
         String formattedValue = sumN + " " + sumU;
         context.write(key, new Text(formattedValue));
    	
    }
}
