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
public final class YahooServiceClient {

    private final Client jerseyClient;
    private final String yahooHost;
    private final UriBuilder baseUrl;

    @Inject
    public YahooServiceClient(Client jerseyClient) {
        this.jerseyClient = jerseyClient;
        this.yahooHost = "query.yahooapis.com";
        this.baseUrl = UriBuilder.fromUri("http://"+yahooHost);
    }

    //http://weather.yahooapis.com/forecastrss?w=2502265

    //http://query.yahooapis.com/v1/public/yql?q=" + qry)


    public String getData(String zipcode) {
        try {
            final String queryStr = "SELECT woeid FROM geo.places WHERE text=" + zipcode + " LIMIT 1";
            final URI uri = baseUrl.clone()
                    .segment("v1", "public", "yql")
                    .queryParam("q", queryStr)
                    .build();

            return jerseyClient.resource(uri)
                    .get(String.class);
        } catch (UniformInterfaceException e) {
            throw Throwables.propagate(e);
        }
    }

}
