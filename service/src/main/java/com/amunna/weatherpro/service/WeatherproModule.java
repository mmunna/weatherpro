package com.amunna.weatherpro.service;

import com.amunna.weatherpro.service.config.WeatherproConfiguration;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sun.jersey.api.client.Client;
import com.yammer.dropwizard.config.Environment;
import com.yammer.metrics.guice.InstrumentationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherproModule extends InstrumentationModule {
    private static final Logger logger = LoggerFactory.getLogger(WeatherproModule.class);

    private Client defaultJerseyClient;
    protected WeatherproConfiguration weatherproConfiguration;
    private Environment dropwizardEnvironment;

    protected WeatherproModule() {}

    public WeatherproModule(WeatherproConfiguration weatherproConfiguration) {
        this.weatherproConfiguration = weatherproConfiguration;
    }

    public WeatherproModule setDefaultJerseyClient(Client defaultJerseyClient) {
        this.defaultJerseyClient = defaultJerseyClient;
        return this;
    }

    public WeatherproModule setDropWizardEnvironment(Environment dropwizardEnvironment) {
        this.dropwizardEnvironment = dropwizardEnvironment;
        return this;
    }

    @Override
    protected void configure() {
        bind(Client.class).toInstance(defaultJerseyClient);
        bind(com.yammer.dropwizard.config.Environment.class).toInstance(dropwizardEnvironment);
    }

    @Provides @Singleton
    private WeatherproConfiguration createWeatherproConfiguration() {
        return weatherproConfiguration;
    }

}
