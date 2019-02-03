package com.test.weather.weather.model;

public class APIData {

    private int day;
    private int hour;
    private double temperature;
    private double pressure;

    public APIData(int day, int hour, double temperature, double pressure) {
        this.day = day;
        this.hour = hour;
        this.temperature = temperature;
        this.pressure = pressure;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

}
