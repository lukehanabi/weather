package com.test.weather.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.weather.weather.model.APIData;
import com.test.weather.weather.model.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Service
public class WeatherDataService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    final RestTemplate restTemplate = new RestTemplate();

    final ObjectMapper om = new ObjectMapper();

    public WeatherDataService() {
    }

    public WeatherData getDataById(String dataId) {
        LOG.debug("--- Inside getDataById() to API ---");

        // TODO: Unicode issue Munchen to be solved!!!
        String fooResourceUrl = "http://api.openweathermap.org/data/2.5/forecast?q=" +
                dataId +"&appid=663928080a513ba860832815587e6a10&units=metric";
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);

        JsonNode json =  null;

        List<APIData> list= new ArrayList<>();
        //Calculate
        try {
            json = om.readTree(response.getBody());
            JsonNode array = json.get("list");

            for (JsonNode node: array) {
                list.add(new APIData(convertToDay(node.get("dt_txt").textValue()),
                        convertToHour(node.get("dt_txt").textValue()),
                        node.get("main").get("temp").asDouble(),
                        node.get("main").get("pressure").asDouble()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // filter and return averages
        return  getAverages(list, dataId);
    }

    public int convertToDay(String date){
        LocalDateTime dateTime = LocalDateTime.parse(date.replace(" ","T"));
        return dateTime.getDayOfMonth();
    }

    public int convertToHour(String date){
        LocalDateTime dateTime = LocalDateTime.parse(date.replace(" ","T"));
        return dateTime.getHour();
    }


    public WeatherData getAverages(List<APIData> list, String dataId){

        LocalDateTime time = LocalDateTime.now();
        int day = time.getDayOfMonth();
        int finalDay = time.plusDays(3).getDayOfMonth();
        int size = list.size();

        double dayValue = 0;
        double nightValue = 0;
        double pressureValue = 0;

        OptionalDouble dayTemp = list.stream().filter(i-> i.getDay()> day && i.getDay() < finalDay)
                .filter(i -> i.getHour() >= 6 && i.getHour() <= 18).mapToDouble(APIData::getTemperature)
                .average();

        if(dayTemp.isPresent()) {
            dayValue = dayTemp.getAsDouble();
        }


        OptionalDouble nightTemp = list.stream().filter(i-> i.getDay()> day && i.getDay() < finalDay)
                .filter(i -> i.getHour() > 18 && i.getHour() < 6).mapToDouble(APIData::getTemperature)
                .average();

        if(nightTemp.isPresent()) {
            nightValue = nightTemp.getAsDouble();
        }

        OptionalDouble pressure = list.stream().filter(i-> i.getDay()> day && i.getDay() < finalDay)
                .mapToDouble(APIData::getPressure).average();

        if(pressure.isPresent()) {
            pressureValue = pressure.getAsDouble();
        }

        return new WeatherData(dataId, dayValue, nightValue, pressureValue);
    }
}
