package com.amunna.weatherpro.service.resources;

import com.amunna.weatherpro.datacollect.DataCollectClient;
import com.amunna.weatherpro.datastore.SimpleDataStore;
import com.amunna.weatherpro.service.config.WeatherproConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.json.JSONObject;
import org.json.XML;

@Singleton
public final class WeatherDataAggregator {

    public static int PRETTY_PRINT_INDENT_FACTOR = 4;
    public static final String TARGET_DATA_COLUMN_NAME = "average";

    @Inject private DataCollectClient dataCollectClient;
    @Inject private SimpleDataStore simpleDataStore;
    @Inject private WeatherproConfiguration weatherproConfiguration;


    public String getWeatherData(String zipCode) {
        String xmlResult = dataCollectClient.getWeatherData(getWOEIDForZipCode(zipCode));

        JSONObject xmlJSONObj = XML.toJSONObject(xmlResult);
        String averageTemp = simpleDataStore.getTableData(weatherproConfiguration.getTargetDataConfiguration().getTargetTable(),
                "texas",
                weatherproConfiguration.getTargetDataConfiguration().getTargetColumnFamily(),
                TARGET_DATA_COLUMN_NAME);

        xmlJSONObj.append("averageTemperature", averageTemp);
        return  xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
    }

    private static String getWOEIDForZipCode(String zipCode) {
        return zipCode;
    }
}
