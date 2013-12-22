package com.amunna.weatherpro.datacollector.service;

import com.amunna.weatherpro.datacollector.manager.DataCollectManager;
import com.amunna.weatherpro.datacollector.manager.ZipcodeCollectManager;
import com.amunna.weatherpro.datacollector.service.config.DataCollectorConfiguration;
import com.amunna.weatherpro.datacollector.service.resources.DataCollectorResource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCollectorService extends Service<DataCollectorConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectorService.class);

    public static void main(String args[]) throws Exception {
        new DataCollectorService().run(args);
    }

    @Override
    public void initialize(Bootstrap<DataCollectorConfiguration> bootstrap) {
        bootstrap.setName("datacollector");
    }

    @Override
    public void run(final DataCollectorConfiguration configuration, final Environment environment) throws Exception {
        logger.info("initializing ...");

        Module dataCollectorModule = buildDataCollectorModule(configuration, environment);
        Injector injector = Guice.createInjector(dataCollectorModule);

        //managers
        //environment.manage(injector.getInstance(ZipcodeCollectManager.class));
        environment.manage(injector.getInstance(DataCollectManager.class));

        //resources
        environment.addResource(injector.getInstance(DataCollectorResource.class));
    }


    protected Module buildDataCollectorModule(final DataCollectorConfiguration dataCollectorConfiguration, final Environment environment) {
        return new DataCollectorModuleBuilder()
                .setConfiguration(dataCollectorConfiguration)
                .setDropWizardEnvironment(environment)
                .build();

    }
}
