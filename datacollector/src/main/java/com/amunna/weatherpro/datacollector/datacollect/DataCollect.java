package com.amunna.weatherpro.datacollector.datacollect;

import com.amunna.weatherpro.datacollector.manager.ZipcodeCollectManager;
import com.amunna.weatherpro.datacollector.parser.SimpleXMLParser;
import com.amunna.weatherpro.datacollector.scheduler.Task;
import com.amunna.weatherpro.datacollector.service.config.DataCollectorConfiguration;
import com.amunna.weatherpro.datastore.SimpleDataStore;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.hadoop.hbase.client.HTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Singleton
public class DataCollect extends Task {

    private static final Logger logger = LoggerFactory.getLogger(DataCollect.class);

    public static final String JOBNAME = "DATACOLLECT";

    private final DataCollectorConfiguration dataCollectorConfiguration;
    private final DataCollector weatherDataCollector;
    private final SimpleXMLParser simpleXMLParser;
    private final SimpleDataStore simpleDataStore;
    private final ZipcodeCollectManager zipcodeCollectManager;

    @Inject
    public DataCollect(DataCollectorConfiguration dataCollectorConfiguration,
                       DataCollector weatherDataCollector,
                       SimpleXMLParser simpleXMLParser,
                       ZipcodeCollectManager zipcodeCollectManager,
                       SimpleDataStore simpleDataStore
                       ) {
        this.dataCollectorConfiguration = dataCollectorConfiguration;
        this.weatherDataCollector = weatherDataCollector;
        this.simpleXMLParser = simpleXMLParser;
        this.zipcodeCollectManager = zipcodeCollectManager;
        this.simpleDataStore = simpleDataStore;
    }
    @Override
    public void execute() throws Exception {
        final List<Map<String,String>> allWoeidData = Lists.newArrayList();
        int counter = 0;
        for (String woeid : zipcodeCollectManager.getWoeidList()) {
            logger.info("downloading weather data for zipcode {}", woeid);
            Map<String, String> weatherData = simpleXMLParser.parseAndGetData(weatherDataCollector.collectData(woeid));
            logger.info("downloaded data for zipcode {}", woeid);
            logger.info(".... date {}", weatherData.get("date"));
            logger.info(".... lowTemp {}", weatherData.get("low"));
            logger.info(".... highTemp {}", weatherData.get("high"));
            logger.info(".... text {}", weatherData.get("text"));
            logger.info(".... processed {} woeid", ++counter);
            //weatherData.put("woeid", woeid);
            weatherData.put("zipCode", zipcodeCollectManager.zipCodeForWoeId(woeid));
            allWoeidData.add(weatherData);
        }

        logger.info("finished collecting weather data of all woeids");
        simpleDataStore.updateTableInBatch(createHTable(),
                    dataCollectorConfiguration.gethBaseConfiguration().getColumnFamily(),
                    allWoeidData);
    }

    @Override
    public String getName() {
        return JOBNAME;
    }

    @Override
    public String getTriggerName() {
        return "datacollect-trigger";
    }

    @Override
    public int getIntervalInHours() {
        return dataCollectorConfiguration.getDataCollectInterval();
    }

    @Override
    public int getIntervalInMinutes() {
        return dataCollectorConfiguration.getDataCollectInterval();
    }

    private HTable createHTable() {
        return simpleDataStore.createTable(dataCollectorConfiguration.gethBaseConfiguration().getTable(),
                dataCollectorConfiguration.gethBaseConfiguration().getColumnFamily());
    }

}
