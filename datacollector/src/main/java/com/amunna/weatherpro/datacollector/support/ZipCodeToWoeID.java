package com.amunna.weatherpro.datacollector.support;

import com.amunna.weatherpro.datacollector.client.YahooClient;
import com.amunna.weatherpro.datacollector.client.YahooServiceClient;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class ZipCodeToWoeID {
    @Inject private YahooServiceClient yahooServiceClient;

    public String convert(String zipCode) {
        return yahooServiceClient.getData(zipCode);
    }
}
