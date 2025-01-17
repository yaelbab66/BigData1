package mostFreqAppDays;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MostFreqAppDaysMapper extends Mapper<LongWritable, Text, Text, IntWritable>{


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    	
    	String[] parts = value.toString().split( ",");
    	
	    String dateString = parts[0].trim();
	    if (! dateString.equals("Date")) {
	        int val = Integer.parseInt(parts[4]);
	        
	        
	        try {
	        	
                //parse the String to Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(dateString);
                //Formating the Date to a day
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
                String dayName = dayFormat.format(date);
                   
                //write the data
                context.write(new Text(dayName), new IntWritable(val));
                
            } catch (ParseException | NumberFormatException e) {
                // Log and skip invalid entries
                System.err.println("Invalid entry: " + value.toString());
            }

	    }
    }
}
	
	
	

