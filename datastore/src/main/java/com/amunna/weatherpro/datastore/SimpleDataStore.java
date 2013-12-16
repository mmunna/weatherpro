package com.amunna.weatherpro.datastore;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.io.ByteToCharEUC_TW;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Singleton
public final class SimpleDataStore {
    private static final Logger logger = LoggerFactory.getLogger(SimpleDataStore.class);
    private static final Configuration config = createConfig();

    public static void main(String args[]) {
        SimpleDataStore simpleDataStore = new SimpleDataStore();
        final String columnFamily = "temperature";
        final String tableName = "diary";
        HBaseAdmin admin;
        HTable hTable = null;
        // If table does not exist create it
        try {
            admin = new HBaseAdmin(config);
            if (!admin.tableExists(tableName)) {
                hTable = simpleDataStore.createTable(tableName, columnFamily);
            } else {
                hTable = new HTable(config, tableName);
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        //update Table
        for (int i = 0; i < 100; i++) {
            final Map<String, String> columnAndValue = Maps.newHashMap();
            final double lowTemp = Math.random() * 100;
            final double highTemp = lowTemp + (Math.random() * (100 - lowTemp));
            columnAndValue.put("low", String.valueOf(lowTemp));
            columnAndValue.put("high", String.valueOf(highTemp));
            final String row = "row" + i;
            simpleDataStore.updateTable(hTable, row, columnFamily, columnAndValue);
        }
        //output table data
        simpleDataStore.scanTableData(hTable, columnFamily, "low", "high");
    }

    public HTable createTable(String tableName, String... columnFamilies) {
        HBaseAdmin admin;
        // If table does not exist create it
        try {
            admin = new HBaseAdmin(config);
            if (admin.tableExists(tableName)) {
                logger.info("table {} already exists", tableName);
                return new HTable(config, tableName);
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }

        logger.info("creating table {}", tableName);
        HTableDescriptor ht = new HTableDescriptor(tableName);
        for (String columnFamily : columnFamilies) {
            ht.addFamily(new HColumnDescriptor(columnFamily));
        }
        logger.info("connecting to hBase cluster....");
        HTable hTable = null;
        try {
            HBaseAdmin hba = new HBaseAdmin(config);
            logger.info("creating table ....");
            hba.createTable(ht);
            hTable = new HTable(config, tableName);
            logger.info("done with table creation ...");
        } catch (MasterNotRunningException e) {
            logger.error("error occurred during hbase admin creation, message: {}", e.getMessage(), e);
            Throwables.propagate(e);
        } catch (ZooKeeperConnectionException e) {
            logger.error("error occurred during hbase admin creation, message: {}", e.getMessage(), e);
            Throwables.propagate(e);
        } catch (IOException e) {
            logger.error("error occurred during table creation, message: {}", e.getMessage(), e);
            Throwables.propagate(e);
        }
        return hTable;
    }

    public void updateTable(HTable table, String row, String columnFamily, Map<String, String> columnAndValue) {
        logger.info("updating table {} columnFamily {}", table.getTableName().toString(), columnFamily);
        if (columnAndValue.size() == 0) return;
        try {
            Put p = new Put(Bytes.toBytes(row));
            for (Map.Entry<String, String> entry : columnAndValue.entrySet()) {
                logger.info("updating column {} with value {}", entry.getKey(), entry.getValue());
                p.add(Bytes.toBytes(columnFamily), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
            }
            table.put(p);
        } catch (IOException e) {
            logger.error("error occurred while updating table, message: {}", e.getMessage(), e);
            Throwables.propagate(e);
        }
    }

    public void updateTableInBatch(HTable table, String columnFamily, List<Map<String, String>> allWoeidData) {
        logger.info("updating table {} columnFamily {} in batch for all woeids", table.getTableName().toString(), columnFamily);
        for (Map<String, String> singleWoeid : allWoeidData) {
            final String woeid = singleWoeid.get("woeid");
            if (woeid == null) continue;
            singleWoeid.remove("woeid");
            updateTable(table, woeid, columnFamily, singleWoeid);
        }
        logger.info("finished updating table {} columnFamily {} in batch for all woeids");
    }

    public String getTableData(String table, String row, String columnFamily, String column) {
        Get g = new Get(Bytes.toBytes(row));
        HTable hTable = createTable(table, columnFamily);
        try {
            Result r = hTable.get(g);
            return Float.toString(Bytes.toFloat(r.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(column))));
        } catch (IOException e) {
            logger.error("error occurred during get table data, message {}", e.getMessage(), e);
            Throwables.propagate(e);
        }
        return null;
    }

    public void scanTableData(HTable table, String columnFamily, String... columns) {
        Scan s = new Scan();
        for (String column : columns) {
            s.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
        }
        ResultScanner scanner = null;
        try {
            scanner = table.getScanner(s);
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                System.out.println("Found row : " + rr);
            }
        } catch (IOException e) {
            logger.error("error occurred during scan table, message: {}", e.getMessage(), e);
            Throwables.propagate(e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static Configuration createConfig() {
        Configuration config = HBaseConfiguration.create();
        config.clear();
        config.set("hbase.zookeeper.quorum", "localhost:2181");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.zookeeper.dns.nameserver", "localhost");
        config.set("hbase.regionserver.port", "60020");
        config.set("hbase.master", "localhost:9000");
        return config;
    }
}
