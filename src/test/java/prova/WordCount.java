package prova;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

	public static void main(String[] args) throws Exception {

		Configuration configuration = new Configuration();

		Job job1 = new Job(configuration, "WordCount");
		job1.setJarByClass(WordCount.class);
		job1.setMapperClass(WordCountMapper.class);
		job1.setReducerClass(WordCountReducer.class);

		FileInputFormat.addInputPath(job1, new Path(args[0]));
		FileOutputFormat.setOutputPath(job1, new Path(args[1]));

		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(IntWritable.class);
		job1.waitForCompletion(true);

		Job job2 = new Job(configuration, "OrderByValue");
		job2.setJarByClass(WordCount.class);
		job2.setMapperClass(OrderByValueMapper.class);
		job2.setSortComparatorClass(IntComparator.class);
		job2.setReducerClass(OrderByValueReducer.class);

		FileInputFormat.addInputPath(job2, new Path(args[1]));
		FileOutputFormat.setOutputPath(job2, new Path(args[2]));

		job2.setOutputKeyClass(IntWritable.class);
		job2.setOutputValueClass(Text.class);
		if (job1.waitForCompletion(true)) {
			job2.waitForCompletion(true);
		}
	}
}