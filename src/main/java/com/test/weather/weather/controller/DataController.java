package com.test.weather.weather.controller;

import com.test.weather.weather.model.WeatherData;
import com.test.weather.weather.service.WeatherDataCacheService;
import com.test.weather.weather.service.WeatherDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("data")
/**
 * Usage:
 * /data/temperature/{city}/next/{days}/average
 * */
public class DataController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private WeatherDataCacheService weatherDataCacheService;

    @Autowired
    private WeatherDataService weatherDataService;

    public DataController(WeatherDataCacheService weatherDataCacheService,
                          WeatherDataService weatherDataService) {
        this.weatherDataCacheService = weatherDataCacheService;
        this.weatherDataService = weatherDataService;
    }

    @GetMapping("temperature/{city}/{country}/next/{days}/average")
    public ResponseEntity<WeatherData> getDataById(@PathVariable("city") String city,
                                                   @PathVariable("country") String country,
                                                   @PathVariable("days") String days) {

        WeatherData data = null;
        try {
            data = weatherDataCacheService.getDataById(city + "," + country);
        } catch(Exception e){
            if (e.getMessage().contains("404")) {
               return new ResponseEntity<WeatherData>(data, HttpStatus.BAD_REQUEST);
            } else if(e.getMessage().contains("400")) {
                return new ResponseEntity<WeatherData>(data, HttpStatus.BAD_REQUEST);
            } else if(e.getMessage().contains("429")) {
                return new ResponseEntity<WeatherData>(data, HttpStatus.TOO_MANY_REQUESTS);
            } else if(e.getMessage().contains("500")) {
                return new ResponseEntity<WeatherData>(data, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<WeatherData>(data, HttpStatus.CREATED);
    }

}
