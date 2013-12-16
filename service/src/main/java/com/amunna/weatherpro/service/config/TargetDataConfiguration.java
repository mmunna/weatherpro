package com.amunna.weatherpro.service.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TargetDataConfiguration {

    @Valid @NotNull
    @JsonProperty
    private String targetTable;

    @Valid @NotNull @JsonProperty
    private String targetColumnFamily;

    public String getTargetTable() {
        return targetTable;
    }

    public String getTargetColumnFamily() {
        return targetColumnFamily;
    }

}
