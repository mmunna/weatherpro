package com.amunna.weatherpro.dataprocessor.datacollect;

import com.amunna.weatherpro.dataprocessor.service.config.DataProcessorConfiguration;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.yammer.dropwizard.client.JerseyClientBuilder;
import com.yammer.dropwizard.config.Environment;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Singleton
public class DataCollectClient {
    private static Client jerseyClient;
    private final String dataCollectHost;
    private static UriBuilder baseUrl;
    private final Environment dropWizardEnvironment;
    private final DataProcessorConfiguration dataProcessorConfiguration;

    @Inject
    public DataCollectClient(DataProcessorConfiguration dataProcessorConfiguration,
                             Environment dropWizardEnvironment) {
        this.dataProcessorConfiguration = dataProcessorConfiguration;
        this.dataCollectHost = dataProcessorConfiguration.getDataCollectHostConfiguration().getHost()+":"+
                dataProcessorConfiguration.getDataCollectHostConfiguration().getPort();
        this.dropWizardEnvironment = dropWizardEnvironment;
        this.baseUrl = UriBuilder.fromUri("http://"+dataCollectHost);
        this.jerseyClient = createJerseyClient();
    }

    public static String getStateNameForZipCode(String zipCode) {
        try {
            final URI uri = baseUrl.clone()
                    .segment("weather","statename")
                    .queryParam("z", zipCode)
                    .build();
            return jerseyClient.resource(uri)
                    .get(String.class);
        } catch (UniformInterfaceException e) {
            throw Throwables.propagate(e);
        }
    }

    private com.sun.jersey.api.client.Client createJerseyClient() {
        return new JerseyClientBuilder()
                .using(dropWizardEnvironment)
                .using(dataProcessorConfiguration.getHttpClientConfiguration())
                .build();
    }
}
