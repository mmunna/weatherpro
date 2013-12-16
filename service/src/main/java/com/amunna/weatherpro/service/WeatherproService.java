package com.amunna.weatherpro.service;

import com.amunna.weatherpro.service.config.WeatherproConfiguration;
import com.amunna.weatherpro.service.resources.WeatherproResource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherproService extends Service<WeatherproConfiguration> {
    private static final Logger logger = LoggerFactory.getLogger(WeatherproService.class);

    public static void main(String args[]) throws Exception {
        new WeatherproService().run(args);
    }

    @Override
    public void initialize(Bootstrap<WeatherproConfiguration> bootstrap) {
        bootstrap.setName("weatherpro");
    }

    @Override
    public void run(final WeatherproConfiguration configuration, final Environment environment) throws Exception {
        logger.info("initializing ...");

        Module weatherproModule = buildWeatherproModule(configuration, environment);
        Injector injector = Guice.createInjector(weatherproModule);

        //resources
        environment.addResource(injector.getInstance(WeatherproResource.class));
    }


    protected Module buildWeatherproModule(final WeatherproConfiguration weatherproConfiguration, final Environment environment) {
        return new WeatherproModuleBuilder()
                .setConfiguration(weatherproConfiguration)
                .setDropWizardEnvironment(environment)
                .build();

    }
}
