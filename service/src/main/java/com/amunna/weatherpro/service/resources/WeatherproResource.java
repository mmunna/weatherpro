package com.amunna.weatherpro.service.resources;

import com.amunna.weatherpro.service.config.WeatherproConfiguration;
import com.google.inject.Inject;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/weather")
@Produces(MediaType.APPLICATION_JSON)
public class WeatherproResource {
    private static final Logger logger = LoggerFactory.getLogger(WeatherproResource.class);

    @Inject private WeatherDataAggregator weatherDataAggregator;

    @GET
    @Timed(group = "amunna.weather", type = "WeatherproResource")
    public String getWeather(@QueryParam("zipcode") @DefaultValue("0000") String zipCode) {
        return weatherDataAggregator.getWeatherData(zipCode);
    }

}
