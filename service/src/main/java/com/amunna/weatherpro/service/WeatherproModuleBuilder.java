package com.amunna.weatherpro.service;

import com.amunna.weatherpro.service.config.WeatherproConfiguration;
import com.yammer.dropwizard.client.JerseyClientBuilder;
import com.yammer.dropwizard.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherproModuleBuilder {
    private static final Logger logger = LoggerFactory.getLogger(WeatherproModuleBuilder.class);

    private WeatherproConfiguration weatherproConfiguration;
    private Environment dropWizardEnvironment;

    public WeatherproModuleBuilder setConfiguration(WeatherproConfiguration weatherproConfiguration) {
        this.weatherproConfiguration = weatherproConfiguration;
        return this;
    }

    public WeatherproModuleBuilder setDropWizardEnvironment(Environment dropWizardEnvironment) {
        this.dropWizardEnvironment = dropWizardEnvironment;
        return this;
    }

    public WeatherproModule build() {
        assertNoneNull(weatherproConfiguration, dropWizardEnvironment);
        return new WeatherproModule(weatherproConfiguration)
                .setDefaultJerseyClient(createJerseyClient())
                .setDropWizardEnvironment(dropWizardEnvironment);
    }

    private com.sun.jersey.api.client.Client createJerseyClient() {
        return new JerseyClientBuilder()
                .using(dropWizardEnvironment)
                .using(weatherproConfiguration.getHttpClientConfiguration())
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
