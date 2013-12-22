package com.amunna.weatherpro.dataprocessor.service.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.client.JerseyClientConfiguration;
import com.yammer.dropwizard.config.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DataProcessorConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("serviceName")
    private String serviceName;

    @Valid @NotNull @JsonProperty("dataProcessInterval")
    private int dataProcessInterval;

    @Valid @NotNull @JsonProperty("hbaseConfiguration")
    private HBaseConfiguration hBaseConfiguration = new HBaseConfiguration();

    @Valid @NotNull @JsonProperty("httpClient")
    private JerseyClientConfiguration httpClientConfiguration = new JerseyClientConfiguration();

    @Valid @NotNull @JsonProperty("dataCollectHost")
    private DataCollectHostConfiguration dataCollectHostConfiguration = new DataCollectHostConfiguration();

    public String getServiceName() {
        return serviceName;
    }

    public int getDataProcessInterval() {
        return dataProcessInterval;
    }

    public HBaseConfiguration gethBaseConfiguration() {
        return hBaseConfiguration;
    }

    public DataCollectHostConfiguration getDataCollectHostConfiguration() {
        return dataCollectHostConfiguration;
    }

    public JerseyClientConfiguration getHttpClientConfiguration() {
        return httpClientConfiguration;
    }

}
