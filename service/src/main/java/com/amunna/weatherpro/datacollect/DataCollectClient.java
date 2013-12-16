package com.amunna.weatherpro.datacollect;

import com.amunna.weatherpro.service.config.WeatherproConfiguration;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Singleton
public class DataCollectClient {
    private final Client jerseyClient;
    private final String dataCollectHost;
    private final UriBuilder baseUrl;

    @Inject
    public DataCollectClient(Client jerseyClient, WeatherproConfiguration weatherproConfiguration) {
        this.jerseyClient = jerseyClient;
        this.dataCollectHost = weatherproConfiguration.getDataCollectHostConfiguration().getHost()+":"+
                weatherproConfiguration.getDataCollectHostConfiguration().getPort();
        this.baseUrl = UriBuilder.fromUri("http://"+dataCollectHost);
    }

    //http://weather.yahooapis.com/forecastrss?w=2502265
    public String getWeatherData(String zipCode) {
        try {
            final URI uri = baseUrl.clone()
                    .segment("weather")
                    .queryParam("z", zipCode)
                    .build();

            return jerseyClient.resource(uri)
                    .get(String.class);
        } catch (UniformInterfaceException e) {
            throw Throwables.propagate(e);
        }
    }
}
