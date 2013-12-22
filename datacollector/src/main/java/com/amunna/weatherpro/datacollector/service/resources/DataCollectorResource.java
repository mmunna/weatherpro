package com.amunna.weatherpro.datacollector.service.resources;

import com.amunna.weatherpro.datacollector.datacollect.DataCollect;
import com.amunna.weatherpro.datacollector.datacollect.DataCollector;
import com.amunna.weatherpro.datacollector.manager.ZipcodeCollectManager;
import com.amunna.weatherpro.datacollector.support.Reply;
import com.amunna.weatherpro.datacollector.support.ZipCodeToWoeID;
import com.google.inject.Inject;
import com.yammer.metrics.annotation.Timed;
import org.jboss.logging.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;

@Path("/weather")
@Produces(MediaType.APPLICATION_JSON)
public final class DataCollectorResource {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectorResource.class);

    @Inject private ZipCodeToWoeID zipCodeToWoeID;
    @Inject private ZipcodeCollectManager zipcodeCollectManager;
    @Inject private DataCollector weatherDataCollector;
    @Inject private DataCollect dataCollect;

    @GET
    @Timed(group = "amunna.weather", type = "DataCollectorResource")
    public String getWeather(@QueryParam("z") @DefaultValue("0000") String zipCode) {
        return weatherDataCollector.collectData(zipcodeCollectManager.woeIdForZipCode(zipCode));
    }

    @POST
    @Path("/upload")
    @Timed(group = "amunna.weather", type = "DataCollectorResource")
    public Reply uploadZipCodeWoeID(InputStream in) {
        try {
            zipcodeCollectManager.uploadZipCodeWoeID(in);
        } catch (Exception e) {
            logger.error("Error occurred while uploading zipcode file to datacollector", e);
            throw new RuntimeException(e);
        }
        return Reply.ok();
    }

    @GET
    @Path("/convert")
    public String convertZipCodeToWoeid(@QueryParam("z") String zipCode) {
        return zipCodeToWoeID.convert(zipCode);
    }

    @GET
    @Path("/statename")
    public String stateNameForZipCode(@QueryParam("z") String zipCode) {
        String stateName = zipcodeCollectManager.getStateNameForZipCode(zipCode);
        if (stateName == null) {
            throw new RuntimeException("Error: zipCode not found. Please provide a legitimate zipcode");
        } else {
            return stateName;
        }
    }

    @POST
    @Path("/download")
    public Reply forceDownloadWeatherData() {
        Thread thread = new Thread(new DataCollectThread());
        thread.start();
        return Reply.ok();
    }

    private class DataCollectThread implements Runnable {

        public void run() {
            try{
                dataCollect.execute();
            } catch (Exception e) {
                logger.error("error occurred while force downloading weather data");
            }
        }
    }
}
