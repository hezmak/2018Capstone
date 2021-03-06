package com.hardcopy.blechat.fragments;


import java.util.Date;

/**
 * Created by 503 on 2018-05-27.
 */

public class Weather {
    private static float humidity;
    private static float temperature;
    private static float dustdensity;
    private static String location;
    private static Date date;

    public static Date getDate() {
        return date;
    }

    public static void setDate(Date date) {
        Weather.date = date;
    }

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        Weather.location = location;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getDustdensity() {
        return dustdensity;
    }

    public void setDustdensity(float dustdensity) {
        this.dustdensity = dustdensity;
    }
}
