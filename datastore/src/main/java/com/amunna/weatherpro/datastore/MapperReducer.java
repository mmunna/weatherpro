package com.amunna.weatherpro.datastore;

import com.google.common.base.Throwables;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;
import java.util.StringTokenizer;

public class MapperReducer {

    private static final String TARGET_TABLE = "output2";
    private static final String TARGET_CF = "stat";
    private static final String INPUT_TABLE = "note";
    private static final String INPUT_CF = "daily";
    private static final String TEXT = "text";

    private static final boolean caseSensitive = true;

    public static void main(String args[]) {
        try {
            Configuration config = HBaseConfiguration.create();
            Job job = new Job(config,"ExampleSummary");
            job.setJarByClass(MapperReducer.class);     // class that contains mapper and reducer

            // create output table for reducer
            new SimpleDataStore().createTable(TARGET_TABLE, TARGET_CF);

            Scan scan = new Scan();
            scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
            scan.setCacheBlocks(false);  // don't set to true for MR jobs, set other attributes if required

            TableMapReduceUtil.initTableMapperJob(
                    INPUT_TABLE,        // input table
                    scan,               // Scan instance to control CF and attribute selection
                    MyMapper.class,     // mapper class
                    Text.class,         // mapper output key
                    IntWritable.class,  // mapper output value
                    job);
            TableMapReduceUtil.initTableReducerJob(
                    TARGET_TABLE,        // output table
                    MyTableReducer.class,    // reducer class
                    job);
            job.setNumReduceTasks(1);   // at least one, adjust as required

            boolean b = job.waitForCompletion(true);
            if (!b) {
                throw new IOException("error with job!");
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }

    }

    public static class MyMapper extends TableMapper<Text, IntWritable>  {
        private final IntWritable ONE = new IntWritable(1);
        private Text text = new Text();

        /*public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
            String val = new String(value.getValue(Bytes.toBytes(INPUT_CF), Bytes.toBytes(TEXT)));
            text.set(val);     // we can only emit Writables...
            context.write(text, ONE);
        }*/

        public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException  {
            final String line = new String(value.getValue(Bytes.toBytes(INPUT_CF), Bytes.toBytes(TEXT))).toString();
            final String caseSensitiveLine = (caseSensitive) ? line : line.toLowerCase();
            final StringTokenizer tokenizer = new StringTokenizer(caseSensitiveLine);
            while (tokenizer.hasMoreTokens()) {
                text.set(tokenizer.nextToken());
                context.write(text, ONE);
            }
        }
    }

    public static class MyTableReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable> {
        public static final byte[] COUNT = "count".getBytes();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int i = 0;
            for (IntWritable val : values) {
                i += val.get();
            }
            Put put = new Put(Bytes.toBytes(key.toString()));
            put.add(Bytes.toBytes(TARGET_CF), COUNT, Bytes.toBytes(i));

            context.write(null, put);
        }
    }
}
