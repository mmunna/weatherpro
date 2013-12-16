package com.amunna.weatherpro.dataprocessor.service.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class HBaseConfiguration {

    @Valid
    @NotNull
    @JsonProperty
    private String srcTable;

    @Valid
    @NotNull
    @JsonProperty
    private String srcColumnFamily;

    @Valid
    @NotNull
    @JsonProperty
    private String destTable;

    @Valid
    @NotNull
    @JsonProperty
    private String destColumnFamily;


    public String getSrcTable() {
        return srcTable;
    }

    public String getSrcColumnFamily() {
        return srcColumnFamily;
    }

    public String getDestTable() {
        return destTable;
    }

    public String getDestColumnFamily() {
        return destColumnFamily;
    }


}
