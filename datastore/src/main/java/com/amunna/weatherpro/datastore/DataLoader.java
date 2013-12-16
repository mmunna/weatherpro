package com.amunna.weatherpro.datastore;

import com.google.common.collect.Maps;
import org.apache.hadoop.hbase.client.HTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;


public class DataLoader {
    private static SimpleDataStore simpleDataStore = new SimpleDataStore();

    public static void main(String args[]) {
        DataLoader testDataLoader = new DataLoader();
        testDataLoader.loadData();
    }

    private void loadData() {

        HTable table = simpleDataStore.createTable("note", "daily");
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/TestData.json")));
            int counter = 1;
            while ((sCurrentLine = br.readLine()) != null) {
                final Map<String, String> columnAndValue = Maps.newHashMap();
                columnAndValue.put("text", sCurrentLine);
                simpleDataStore.updateTable(table, "row"+counter, "daily", columnAndValue);
                ++counter;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
