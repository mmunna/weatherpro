package com.amunna.weatherpro.datacollector.support;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Reply {

    public static Reply ok() {
        return new Reply(true);
    }

    public static Reply ok(String message) {
        return new Reply(true, message);
    }

    @JsonProperty
    private final boolean ok;

    @JsonProperty
    private final String message;

    /** NOTE: constructor must remain public for Jackson/Jersey serialization! */
    public Reply(boolean ok) {
        this(ok, "just ok");
    }

    public Reply(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

}
