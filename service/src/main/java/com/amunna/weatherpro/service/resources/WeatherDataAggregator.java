package com.amunna.weatherpro.service.resources;

import com.amunna.weatherpro.datacollect.DataCollectClient;
import com.amunna.weatherpro.datastore.SimpleDataStore;
import com.amunna.weatherpro.service.config.WeatherproConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

@Singleton
public final class WeatherDataAggregator {
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataAggregator.class);

    public static int PRETTY_PRINT_INDENT_FACTOR = 4;
    public static final String TARGET_DATA_COLUMN_NAME = "average";

    @Inject private DataCollectClient dataCollectClient;
    @Inject private SimpleDataStore simpleDataStore;
    @Inject private WeatherproConfiguration weatherproConfiguration;


    public String getWeatherData(String zipCode) {
        String xmlResult = null;
        try {
            xmlResult = dataCollectClient.getWeatherData(zipCode);
        } catch (Exception e) {
            throw new RuntimeException("error occurred while downloading wether data from Yahoo for zipcode " + zipCode);
        }
        String stateName = dataCollectClient.getStateNameForZipCode(zipCode);

        JSONObject xmlJSONObj = XML.toJSONObject(xmlResult);
        String averageTemp = null;
        try{
            averageTemp = simpleDataStore.getTableData(weatherproConfiguration.getTargetDataConfiguration().getTargetTable(),
                    stateName,
                    weatherproConfiguration.getTargetDataConfiguration().getTargetColumnFamily(),
                    TARGET_DATA_COLUMN_NAME);
        } catch (Exception e) {
            logger.error("error occurred while gettig average temperature data from datastore for state {} and zipcode {}", stateName, zipCode, e);
        }

        xmlJSONObj.append("averageTemperature", averageTemp);
        return  xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
    }
}
