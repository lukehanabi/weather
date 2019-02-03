package com.test.weather.weather.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("WeatherData")
public class WeatherData implements Serializable {

    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    private String dataId;
    private double dayAverage;
    private double nightAverage;
    private double pressureAvg;

    public WeatherData(String dataId, double dayAverage,
                       double nightAverage, double pressureAvg) {
        this.dayAverage = dayAverage;
        this.nightAverage = nightAverage;
        this.pressureAvg = pressureAvg;
        this.dataId = dataId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public double getDayAverage() {
        return dayAverage;
    }

    public void setDayAverage(double dayAverage) {
        this.dayAverage = dayAverage;
    }

    public double getNightAverage() {
        return nightAverage;
    }

    public void setNightAverage(double nightAverage) {
        this.nightAverage = nightAverage;
    }

    public double getPressureAvg() {
        return pressureAvg;
    }

    public void setPressureAvg(double pressureAvg) {
        this.pressureAvg = pressureAvg;
    }
}
