package com.test.weather.weather.service;

import com.test.weather.weather.model.WeatherData;
import com.test.weather.weather.repository.WeatherDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WeatherDataCacheService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private WeatherDataService weatherDataService;

    public WeatherDataCacheService(WeatherDataRepository weatherDataRepository,
                                   WeatherDataService weatherDataService) {
        this.weatherDataRepository = weatherDataRepository;
        this.weatherDataService = weatherDataService;
    }

    @Cacheable(value= "weatherDataCache", key= "#dataId")
    public WeatherData getDataById(String dataId) {
        LOG.debug("--- Inside getDataById() ---");

        WeatherData data = null;

        try {
            data = weatherDataRepository.findById(dataId).get();
        } catch(NoSuchElementException e) {
            data = weatherDataService.getDataById(dataId);
            weatherDataRepository.save(data);
        }
        return data;

    }

    @Cacheable(value= "weatherDataCache", unless= "#result.size() == 0")
    public List<WeatherData> getAllData(){
        LOG.debug("--- Inside getAllData() ---");
        List<WeatherData> list = new ArrayList<>();
        weatherDataRepository.findAll().forEach(e -> list.add(e));
        return list;
    }

    @Caching(
            put= { @CachePut(value= "weatherDataCache", key= "#data.dataId") },
            evict= { @CacheEvict(value= "weatherDataCache", allEntries= true) }
    )
    public WeatherData addTemperature(WeatherData data){
        LOG.debug("--- Inside addData() ---");
        return weatherDataRepository.save(data);
    }

    @Caching(
            put= { @CachePut(value= "articleCache", key= "#data.dataId") },
            evict= { @CacheEvict(value= "weatherDataCache", allEntries= true) }
    )
    public WeatherData updateTemperature(WeatherData data) {
        LOG.debug("--- Inside updateData() ---");
        return weatherDataRepository.save(data);
    }

    @Caching(
            evict= {
                    @CacheEvict(value= "weatherDataCache", key= "#dataId"),
                    @CacheEvict(value= "weatherDataCache", allEntries= true)
            }
    )
    public void deleteTemperature(String dataId) {
        LOG.debug("--- Inside deleteData() ---");
        weatherDataRepository.delete(weatherDataRepository.findById(dataId).get());
    }
}
