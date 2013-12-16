package com.amunna.weatherpro.datacollector.service.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class HBaseConfiguration {
    @Valid @NotNull @JsonProperty
    private String table;

    @Valid @NotNull @JsonProperty
    private String columnFamily;

    public String getTable() {
        return table;
    }

    public String getColumnFamily() {
        return columnFamily;
    }
}
