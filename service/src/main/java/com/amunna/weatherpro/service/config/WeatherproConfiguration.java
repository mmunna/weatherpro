package com.amunna.weatherpro.service.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.client.JerseyClientConfiguration;
import com.yammer.dropwizard.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class WeatherproConfiguration extends Configuration {

    @Valid @NotNull @JsonProperty("serviceName")
    private String serviceName;

    @Valid @NotNull @JsonProperty("httpClient")
    private JerseyClientConfiguration httpClientConfiguration = new JerseyClientConfiguration();

    @Valid @NotNull @JsonProperty("dataCollectHost")
    private DataCollectHostConfiguration dataCollectHostConfiguration = new DataCollectHostConfiguration();

    @Valid @NotNull @JsonProperty("targetData")
    private TargetDataConfiguration targetDataConfiguration = new TargetDataConfiguration();

    public String getServiceName() {
        return serviceName;
    }

    public JerseyClientConfiguration getHttpClientConfiguration() {
        return httpClientConfiguration;
    }

    public DataCollectHostConfiguration getDataCollectHostConfiguration() {
        return dataCollectHostConfiguration;
    }

    public TargetDataConfiguration getTargetDataConfiguration() {
        return targetDataConfiguration;
    }

}
