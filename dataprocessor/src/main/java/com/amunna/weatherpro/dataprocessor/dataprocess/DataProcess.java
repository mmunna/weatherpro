package com.amunna.weatherpro.dataprocessor.dataprocess;

import com.amunna.weatherpro.dataprocessor.scheduler.Task;
import com.amunna.weatherpro.datastore.SimpleDataStore;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.hadoop.hbase.client.HTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import com.amunna.weatherpro.dataprocessor.service.config.DataProcessorConfiguration;
@Singleton
public class DataProcess extends Task {

    private static final Logger logger = LoggerFactory.getLogger(DataProcess.class);

    public static final String JOBNAME = "DATAPROCESS";

    private final DataProcessorConfiguration DataProcessorConfiguration;
    private final DataProcessor weatherDataProcessor;

    @Inject
    public DataProcess(DataProcessorConfiguration DataProcessorConfiguration,
                       DataProcessor weatherDataProcessor
    ) {
        this.DataProcessorConfiguration = DataProcessorConfiguration;
        this.weatherDataProcessor = weatherDataProcessor;
    }
    @Override
    public void execute() throws Exception {
        weatherDataProcessor.computeAverageTemp();
    }

    @Override
    public String getName() {
        return JOBNAME;
    }

    @Override
    public String getTriggerName() {
        return "dataprocess-trigger";
    }

    @Override
    public int getIntervalInHours() {
        return DataProcessorConfiguration.getDataProcessInterval();
    }

    @Override
    public int getIntervalInMinutes() {
        return DataProcessorConfiguration.getDataProcessInterval();
    }
}
