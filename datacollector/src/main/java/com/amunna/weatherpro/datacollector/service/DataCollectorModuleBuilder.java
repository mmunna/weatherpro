package com.amunna.weatherpro.datacollector.service;

import com.amunna.weatherpro.datacollector.service.config.DataCollectorConfiguration;
import com.yammer.dropwizard.client.JerseyClientBuilder;
import com.yammer.dropwizard.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCollectorModuleBuilder {
    private static final Logger logger = LoggerFactory.getLogger(DataCollectorModuleBuilder.class);

    private DataCollectorConfiguration dataCollectorConfiguration;
    private Environment dropWizardEnvironment;

    public DataCollectorModuleBuilder setConfiguration(DataCollectorConfiguration dataCollectorConfiguration) {
        this.dataCollectorConfiguration = dataCollectorConfiguration;
        return this;
    }

    public DataCollectorModuleBuilder setDropWizardEnvironment(Environment dropWizardEnvironment) {
        this.dropWizardEnvironment = dropWizardEnvironment;
        return this;
    }

    public DataCollectorModule build() {
        assertNoneNull(dataCollectorConfiguration, dropWizardEnvironment);
        return new DataCollectorModule(dataCollectorConfiguration)
                .setDefaultJerseyClient(createJerseyClient())
                .setDropWizardEnvironment(dropWizardEnvironment);
    }

    private com.sun.jersey.api.client.Client createJerseyClient() {
        return new JerseyClientBuilder()
                .using(dropWizardEnvironment)
                .using(dataCollectorConfiguration.getHttpClientConfiguration())
                .build();
    }

    private static void assertNoneNull(Object... values) {
        for (Object value : values) {
            if (value == null) {
                throw new IllegalStateException();
            }
        }
    }
}
