package com.amunna.weatherpro.dataprocessor.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yammer.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amunna.weatherpro.dataprocessor.scheduler.DataProcessScheduler;
import com.amunna.weatherpro.dataprocessor.dataprocess.DataProcess;

@Singleton
public final class DataProcessManager implements Managed {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessManager.class);

    private final DataProcessScheduler DataProcessScheduler;
    private final DataProcess DataProcess;

    @Inject
    public DataProcessManager(DataProcessScheduler DataProcessScheduler,
                              DataProcess DataProcess) {
        this.DataProcessScheduler = DataProcessScheduler;
        this.DataProcess = DataProcess;
    }

    @Override
    public void start() throws Exception {
        DataProcessScheduler.start();
        DataProcessScheduler.addTask(DataProcess.getJobDetail(), DataProcess.getTriggerToStartNowAndRepeatInMinutes());
    }

    @Override
    public void stop() throws Exception {
        DataProcessScheduler.shutdown();
    }
}