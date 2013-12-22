package com.amunna.weatherpro.dataprocessor.service.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DataCollectHostConfiguration {

    @Valid
    @NotNull
    @JsonProperty
    private String host;

    @Valid @NotNull @JsonProperty
    private String port;

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

}
