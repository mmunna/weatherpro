package com.amunna.weatherpro.dataprocessor.dataprocess;

import com.amunna.weatherpro.dataprocessor.datacollect.DataCollectClient;
import com.amunna.weatherpro.dataprocessor.dataprocess.datasplit.SimpleTableSplitter;
import com.amunna.weatherpro.dataprocessor.service.config.DataProcessorConfiguration;
import com.amunna.weatherpro.datastore.SimpleDataStore;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Singleton
public final class WeatherDataProcessor implements DataProcessor {
    private static final Logger logger = LoggerFactory.getLogger("WeatherDataProcessor.class");

    private final SimpleDataStore simpleDataStore;
    private final DataProcessorConfiguration dataProcessorConfiguration;
    private final DataCollectClient dataCollectClient;

    private static String srcCF;
    private static String destCF;
    private static String destColumn = "average";

    @Inject
    public WeatherDataProcessor(SimpleDataStore simpleDataStore,
                               DataProcessorConfiguration dataProcessorConfiguration,
                               DataCollectClient dataCollectClient) {
        this.simpleDataStore = simpleDataStore;
        this.dataProcessorConfiguration = dataProcessorConfiguration;
        this.dataCollectClient = dataCollectClient;
        this.srcCF = dataProcessorConfiguration.gethBaseConfiguration().getSrcColumnFamily();
        this.destCF = dataProcessorConfiguration.gethBaseConfiguration().getDestColumnFamily();
    }

    public void computeAverageTemp() {
        try {
            Configuration config = HBaseConfiguration.create();
            Job job = new Job(config,"WeatherAverageCompute");
            job.setJarByClass(WeatherDataProcessor.class);     // class that contains mapper and reducer

            // create output table for reducer
            simpleDataStore.createTable(dataProcessorConfiguration.gethBaseConfiguration().getDestTable(),
                    dataProcessorConfiguration.gethBaseConfiguration().getDestColumnFamily());

            Scan scan = new Scan();
            scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
            scan.setCacheBlocks(false);  // don't set to true for MR jobs, set other attributes if required

            TableMapReduceUtil.initTableMapperJob(
                    dataProcessorConfiguration.gethBaseConfiguration().getSrcTable(),        // input table
                    scan,               // Scan instance to control CF and attribute selection
                    WeatherMapper.class,     // mapper class
                    Text.class,         // mapper output key
                    IntWritable.class,  // mapper output value
                    job,
                    true,
                    SimpleTableSplitter.class);


            TableMapReduceUtil.initTableReducerJob(
                    dataProcessorConfiguration.gethBaseConfiguration().getDestTable(),        // output table
                    WeatherReducer.class,    // reducer class
                    job);
            job.setNumReduceTasks(5);   // at least one, adjust as required

            boolean b = job.waitForCompletion(true);
            if (!b) {
                throw new IOException("error with job!");
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }

    }

    public static class WeatherMapper extends TableMapper<Text, IntWritable> {
        private Text text = new Text();

        public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException  {
            final int low = Integer.parseInt(new String(value.getValue(Bytes.toBytes(srcCF), Bytes.toBytes("low"))).toString());
            final int high = Integer.parseInt(new String(value.getValue(Bytes.toBytes(srcCF), Bytes.toBytes("high"))).toString());
            final int total = low+high;
            try {
                text.set(DataCollectClient.getStateNameForZipCode(Bytes.toString(row.get())));
                context.write(text, new IntWritable(total));
            } catch (Exception e ) {
                logger.error("error occurred while getting statename for woeid");
            }
        }
    }

    public static class WeatherReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable> {

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            float sumTemp = 0;
            int dataPoints = 0;
            for (IntWritable val : values) {
                sumTemp += val.get()/(2.0);
                ++dataPoints;
            }
            float average = sumTemp/dataPoints;
            Put put = new Put(Bytes.toBytes(key.toString()));
            put.add(Bytes.toBytes(destCF),Bytes.toBytes(destColumn), Bytes.toBytes(average));
            context.write(null, put);
        }
    }
}
