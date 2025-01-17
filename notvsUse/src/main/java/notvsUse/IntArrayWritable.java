package notvsUse;



import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;

public class IntArrayWritable extends ArrayWritable {
    // No-argument constructor required for Hadoop deserialization
    public IntArrayWritable() {
        super(IntWritable.class);
    }

    // Constructor to initialize the array
    public IntArrayWritable(IntWritable[] values) {
        super(IntWritable.class, values);
    }
}
