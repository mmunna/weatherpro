package com.amunna.weatherpro.datacollector.manager;

import com.amunna.weatherpro.datacollector.service.config.DataCollectorConfiguration;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yammer.dropwizard.lifecycle.Managed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Singleton
public final class ZipcodeCollectManager implements Managed {

    @Inject private DataCollectorConfiguration dataCollectorConfiguration;

    private final Map<String/*zipCode*/,String/*woeid*/> zipCodeWoeIDMap = Maps.newHashMap();
    private final Map<String/*zipCode*/,String/*state*/> zipCodeStateNameMap = Maps.newHashMap();
    private final List<String/*woeid*/> woeidList = Lists.newArrayList();
    private final Map<String/*woeId*/, String/*zipCode*/> woeIDZipCodeMap = Maps.newHashMap();


    public void start() throws Exception {
        //readFile();
    }

    public void stop() throws Exception {}

    public List<String> getWoeidList() {
        return woeidList;
    }

    public String woeIdForZipCode(String zipCode){
        return zipCodeWoeIDMap.get(zipCode);
    }

    public String zipCodeForWoeId(String woeID) {
        return woeIDZipCodeMap.get(woeID);
    }

    // ex. TX, 78729, 1242223
    public void uploadZipCodeWoeID(InputStream in) {
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                String tokens[] = line.split(",");
                zipCodeStateNameMap.put(tokens[1], tokens[0]);
                zipCodeWoeIDMap.put(tokens[1],tokens[2]);
                woeidList.add(tokens[2]);
                woeIDZipCodeMap.put(tokens[2],tokens[1]);
            }
        } catch (IOException e) {
            Throwables.propagate(e);
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                Throwables.propagate(ex);
            }
        }
    }

    public String getStateNameForZipCode(String woeID) {
        return zipCodeStateNameMap.get(woeID);
    }

}
