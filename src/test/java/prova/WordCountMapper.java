package prova;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class WordCountMapper extends
		Mapper<LongWritable, Text, Text, IntWritable> {

	private static final IntWritable one = new IntWritable(1);
	private Text product = new Text();

	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String line = value.toString();
		String allProducts = "";

		StringTokenizer tokenizer = new StringTokenizer(line, "\n");
		
		String products = "";	
		
		while (tokenizer.hasMoreTokens()) {
			products = tokenizer.nextToken().substring(11);
			allProducts=allProducts+products+",";
		}
		
		tokenizer = new StringTokenizer(allProducts, ",");

		while (tokenizer.hasMoreTokens()) {
 			product.set(tokenizer.nextToken());
			context.write(product, one);
		}
	}

}