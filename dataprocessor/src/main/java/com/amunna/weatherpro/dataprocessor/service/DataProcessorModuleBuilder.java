package com.amunna.weatherpro.dataprocessor.service;

import com.yammer.dropwizard.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amunna.weatherpro.dataprocessor.service.config.DataProcessorConfiguration;

public class DataProcessorModuleBuilder {
    private static final Logger logger = LoggerFactory.getLogger(DataProcessorModuleBuilder.class);

    private DataProcessorConfiguration DataProcessorConfiguration;
    private Environment dropWizardEnvironment;

    public DataProcessorModuleBuilder setConfiguration(DataProcessorConfiguration DataProcessorConfiguration) {
        this.DataProcessorConfiguration = DataProcessorConfiguration;
        return this;
    }

    public DataProcessorModuleBuilder setDropWizardEnvironment(Environment dropWizardEnvironment) {
        this.dropWizardEnvironment = dropWizardEnvironment;
        return this;
    }

    public DataProcessorModule build() {
        assertNoneNull(DataProcessorConfiguration, dropWizardEnvironment);
        return new DataProcessorModule(DataProcessorConfiguration)
                .setDropWizardEnvironment(dropWizardEnvironment);
    }


    private static void assertNoneNull(Object... values) {
        for (Object value : values) {
            if (value == null) {
                throw new IllegalStateException();
            }
        }
    }
}
