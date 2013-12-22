package com.amunna.weatherpro.dataprocessor.dataprocess.datasplit;

import com.amunna.weatherpro.datastore.config.DataStoreConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableSplit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SimpleTableSplitter extends TableInputFormat {

    @Override
    public List<InputSplit> getSplits(JobContext context)
            throws IOException {
        List<InputSplit> splits = new ArrayList<InputSplit>();
        Scan scan = getScan();
        for(int i=1; i<=9; i++) {
            //Scan scan = getScan();
            scan.setStartRow(Bytes.toBytes(i-1+"0000"));
            //scan.setStartRow(Bytes.toBytes(i+""));
            if (i==9) {
                scan.setStopRow(Bytes.toBytes("999990"));
            } else {
                scan.setStopRow(Bytes.toBytes(i+"0000"));
                //scan.setStopRow(Bytes.toBytes(i+1+""));
            }
            setScan(scan);

            for (InputSplit subSplit : super.getSplits(context)) {
                /*splits.add((InputSplit) ReflectionUtils.copy(DataStoreConfiguration.configuration,
                        (TableSplit) subSplit, new TableSplit()));*/
                splits.add(subSplit);
            }
        }
        return splits;
    }
}
