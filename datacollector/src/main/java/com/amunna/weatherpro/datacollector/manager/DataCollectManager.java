package com.amunna.weatherpro.datacollector.manager;

import com.amunna.weatherpro.datacollector.datacollect.DataCollect;
import com.amunna.weatherpro.datacollector.scheduler.DataCollectScheduler;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yammer.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public final class DataCollectManager implements Managed {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectManager.class);

    private final DataCollectScheduler dataCollectScheduler;
    private final DataCollect dataCollect;

    @Inject
    public DataCollectManager(DataCollectScheduler dataCollectScheduler,
                              DataCollect dataCollect) {
        this.dataCollectScheduler = dataCollectScheduler;
        this.dataCollect = dataCollect;
    }

    @Override
    public void start() throws Exception {
        dataCollectScheduler.start();
        dataCollectScheduler.addTask(dataCollect.getJobDetail(), dataCollect.getTriggerToStartNowAndRepeatInMinutes());
    }

    @Override
    public void stop() throws Exception {
        dataCollectScheduler.shutdown();
    }
}
