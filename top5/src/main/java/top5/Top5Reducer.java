package top5;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Top5Reducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private TreeMap<Integer, String> top5Map = new TreeMap<>(Comparator.reverseOrder());

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }

        // Add the sum and key to the map
        top5Map.put(sum, key.toString());

        // Keep only the top 5 results in the map
        if (top5Map.size() > 5) {
            top5Map.pollLastEntry(); // Remove the smallest entry
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        // Emit the top 5 results in descending order
        for (Map.Entry<Integer, String> entry : top5Map.entrySet()) {
            context.write(new Text(entry.getValue()), new IntWritable(entry.getKey()));
        }
    }
}
