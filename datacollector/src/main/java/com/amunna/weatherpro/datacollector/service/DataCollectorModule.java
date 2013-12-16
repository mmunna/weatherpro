package com.amunna.weatherpro.datacollector.service;

import com.amunna.weatherpro.datacollector.datacollect.DataCollector;
import com.amunna.weatherpro.datacollector.datacollect.WeatherDataCollector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sun.jersey.api.client.Client;
import com.yammer.dropwizard.config.Environment;
import com.yammer.metrics.guice.InstrumentationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amunna.weatherpro.datacollector.service.config.DataCollectorConfiguration;

public class DataCollectorModule extends InstrumentationModule {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectorModule.class);

    private Client defaultJerseyClient;
    protected DataCollectorConfiguration DataCollectorConfiguration;
    private Environment dropwizardEnvironment;

    public DataCollectorModule(DataCollectorConfiguration DataCollectorConfiguration) {
        this.DataCollectorConfiguration = DataCollectorConfiguration;
    }

    public DataCollectorModule setDefaultJerseyClient(Client defaultJerseyClient) {
        this.defaultJerseyClient = defaultJerseyClient;
        return this;
    }

    public DataCollectorModule setDropWizardEnvironment(Environment dropwizardEnvironment) {
        this.dropwizardEnvironment = dropwizardEnvironment;
        return this;
    }

    @Override
    protected void configure() {
        bind(Client.class).toInstance(defaultJerseyClient);
        bind(com.yammer.dropwizard.config.Environment.class).toInstance(dropwizardEnvironment);
        bind(DataCollector.class).to(WeatherDataCollector.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    private DataCollectorConfiguration createDataCollectorConfiguration() {
        return DataCollectorConfiguration;
    }

}
