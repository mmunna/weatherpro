package com.amunna.weatherpro.datacollector.datacollect;

import com.amunna.weatherpro.datacollector.client.YahooClient;
import com.amunna.weatherpro.datacollector.datacollect.DataCollector;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class WeatherDataCollector implements DataCollector {

    @Inject private YahooClient yahooClient;

    public String collectData(String woeid) {
        return yahooClient.getWeatherData(woeid);
    }
}
