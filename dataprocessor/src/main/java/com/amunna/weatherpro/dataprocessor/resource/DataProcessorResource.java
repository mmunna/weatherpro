package com.amunna.weatherpro.dataprocessor.resource;

import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/weather")
@Produces(MediaType.APPLICATION_JSON)
public final class DataProcessorResource {
    private static final Logger logger = LoggerFactory.getLogger(DataProcessorResource.class);

    @GET
    @Path("/average")
    @Timed(group = "amunna.weather", type = "WeatherproResource")
    public String getAverage() {
        return "success";
    }
}

