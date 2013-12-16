package com.amunna.weatherpro.datacollector.service.resources;

import com.amunna.weatherpro.datacollector.datacollect.DataCollector;
import com.amunna.weatherpro.datacollector.manager.ZipcodeCollectManager;
import com.amunna.weatherpro.datacollector.support.ZipCodeToWoeID;
import com.google.inject.Inject;
import com.yammer.metrics.annotation.Timed;
import org.jboss.logging.Param;
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
public final class DataCollectorResource {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectorResource.class);

    @Inject private ZipCodeToWoeID zipCodeToWoeID;
    @Inject private ZipcodeCollectManager zipcodeCollectManager;
    @Inject private DataCollector weatherDataCollector;

    @GET
    @Timed(group = "amunna.weather", type = "WeatherproResource")
    public String getWeather(@QueryParam("z") @DefaultValue("0000") String zipCode) {
        return weatherDataCollector.collectData(zipcodeCollectManager.woeIdForZipCode(zipCode));
    }

    @GET
    @Path("/convert")
    public String convertZipCodeToWoeid(@QueryParam("z") String zipCode) {
        return zipCodeToWoeID.convert(zipCode);
    }

}
