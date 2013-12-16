package com.amunna.weatherpro.datacollector.service.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.client.JerseyClientConfiguration;
import com.yammer.dropwizard.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DataCollectorConfiguration extends Configuration {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectorConfiguration.class);

    @Valid
    @NotNull
    @JsonProperty("serviceName")
    private String serviceName;

    @Valid @NotNull @JsonProperty("httpClient")
    private JerseyClientConfiguration httpClientConfiguration = new JerseyClientConfiguration();

    @Valid @NotNull @JsonProperty("yahooHost")
    private String yahooHost;

    @Valid @NotNull @JsonProperty("dataCollectInterval")
    private int dataCollectInterval;

    @Valid @NotNull @JsonProperty("hbaseConfiguration")
    private HBaseConfiguration hBaseConfiguration = new HBaseConfiguration();

    @Valid @NotNull @JsonProperty("zipwoeidsrc")
    private String zipwoeidsrc;

    public String getServiceName() {
        return serviceName;
    }

    public JerseyClientConfiguration getHttpClientConfiguration() {
        return httpClientConfiguration;
    }

    public String getYahooHost() {
        return yahooHost;
    }

    public int getDataCollectInterval() {
        return dataCollectInterval;
    }

    public HBaseConfiguration gethBaseConfiguration() {
        return hBaseConfiguration;
    }

    public String getZipwoeidsrc() {
        return zipwoeidsrc;
    }
}
