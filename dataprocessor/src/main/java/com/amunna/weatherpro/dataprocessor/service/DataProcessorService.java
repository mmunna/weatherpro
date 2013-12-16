package com.amunna.weatherpro.dataprocessor.service;

import com.amunna.weatherpro.dataprocessor.manager.DataProcessManager;
import com.amunna.weatherpro.dataprocessor.resource.DataProcessorResource;
import com.amunna.weatherpro.dataprocessor.service.config.DataProcessorConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataProcessorService extends Service<DataProcessorConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessorService.class);

    public static void main(String args[]) throws Exception {
        new DataProcessorService().run(args);
    }

    @Override
    public void initialize(Bootstrap<DataProcessorConfiguration> bootstrap) {
        bootstrap.setName("dataprocessor");
    }

    @Override
    public void run(final DataProcessorConfiguration configuration, final Environment environment) throws Exception {
        logger.info("initializing ...");

        Module dataProcessorModule = builddataProcessorModule(configuration, environment);
        Injector injector = Guice.createInjector(dataProcessorModule);

        //managers
        environment.manage(injector.getInstance(DataProcessManager.class));

        //resources
        environment.addResource(injector.getInstance(DataProcessorResource.class));
    }


    protected Module builddataProcessorModule(final DataProcessorConfiguration DataProcessorConfiguration, final Environment environment) {
        return new DataProcessorModuleBuilder()
                .setConfiguration(DataProcessorConfiguration)
                .setDropWizardEnvironment(environment)
                .build();

    }
}

