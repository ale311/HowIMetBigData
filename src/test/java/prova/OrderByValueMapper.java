package prova;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class OrderByValueMapper extends
		Mapper<LongWritable, Text, IntWritable, Text> {
	
	private Text product = new Text();
	private IntWritable frequency = new IntWritable();

	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		
		String line = value.toString();

		StringTokenizer tokenizer = new StringTokenizer(line);
		
		product.set(tokenizer.nextToken());
		frequency.set(Integer.parseInt(tokenizer.nextToken()));
			
		context.write(frequency, product);
		
	}
}
