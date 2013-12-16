package com.amunna.weatherpro.dataprocessor.service;

import com.amunna.weatherpro.dataprocessor.dataprocess.DataProcessor;
import com.amunna.weatherpro.dataprocessor.dataprocess.WeatherDataProcessor;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.yammer.dropwizard.config.Environment;
import com.yammer.metrics.guice.InstrumentationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amunna.weatherpro.dataprocessor.service.config.DataProcessorConfiguration;

public class DataProcessorModule extends InstrumentationModule {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessorModule.class);

    protected DataProcessorConfiguration DataProcessorConfiguration;
    private Environment dropwizardEnvironment;

    public DataProcessorModule(DataProcessorConfiguration DataProcessorConfiguration) {
        this.DataProcessorConfiguration = DataProcessorConfiguration;
    }

    public DataProcessorModule setDropWizardEnvironment(Environment dropwizardEnvironment) {
        this.dropwizardEnvironment = dropwizardEnvironment;
        return this;
    }

    @Override
    protected void configure() {
        bind(com.yammer.dropwizard.config.Environment.class).toInstance(dropwizardEnvironment);
        bind(DataProcessor.class).to(WeatherDataProcessor.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    private DataProcessorConfiguration createDataProcessorConfiguration() {
        return DataProcessorConfiguration;
    }

}

