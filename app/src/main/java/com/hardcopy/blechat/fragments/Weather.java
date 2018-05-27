package com.hardcopy.blechat.fragments;

/**
 * Created by 503 on 2018-05-27.
 */

public class Weather {
    private static float humidity;
    private static float temperature;
    private static float dustdensity;

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
