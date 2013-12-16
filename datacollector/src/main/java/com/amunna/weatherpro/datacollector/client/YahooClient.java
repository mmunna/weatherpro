package com.amunna.weatherpro.datacollector.client;

import com.amunna.weatherpro.datacollector.service.config.DataCollectorConfiguration;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Singleton
public class YahooClient {
    private final Client jerseyClient;
    private final String yahooHost;
    private final UriBuilder baseUrl;

    @Inject
    public YahooClient(Client jerseyClient, DataCollectorConfiguration dataCollectorConfiguration) {
        this.jerseyClient = jerseyClient;
        this.yahooHost = dataCollectorConfiguration.getYahooHost();
        this.baseUrl = UriBuilder.fromUri("http://"+yahooHost);
    }

    //http://weather.yahooapis.com/forecastrss?w=2502265
    public String getWeatherData(String woeid) {
        try {
            final URI uri = baseUrl.clone()
                    .segment("forecastrss")
                    .queryParam("w", woeid)
                    .build();

            return jerseyClient.resource(uri)
                    .get(String.class);
        } catch (UniformInterfaceException e) {
            throw Throwables.propagate(e);
        }
    }

}
