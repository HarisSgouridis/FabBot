package com.jagrosh.jmusicbot.utils;

import java.io.Serializable;

public class TemperatureInstance implements  Serializable {

    private String location;
    private String time;
    private double temperature;
    private String winddirection;
    private String alarmtext;
    private String expectedWeather;
    private String kindOfWeather;

    public TemperatureInstance(String location, String time, double temperature, String winddirection, String alarmtext, String expectedWeather, String kindOfWeather) {
        this.location = location;
        this.time = time;
        this.temperature = temperature;
        this.winddirection = winddirection;
        this.alarmtext = alarmtext;
        this.expectedWeather = expectedWeather;
        this.kindOfWeather = kindOfWeather;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getWinddirection() {
        return winddirection;
    }

    public String getAlarmtext() {
        return alarmtext;
    }

    public String getExpectedWeather() {
        return expectedWeather;
    }

    public String getKindOfWeather() {
        return kindOfWeather;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setWinddirection(String winddirection) {
        this.winddirection = winddirection;
    }

    public void setAlarmtext(String alarmtext) {
        this.alarmtext = alarmtext;
    }

    public void setExpectedWeather(String expectedWeather) {
        this.expectedWeather = expectedWeather;
    }

    public void setKindOfWeather(String kindOfWeather) {
        this.kindOfWeather = kindOfWeather;
    }
}
