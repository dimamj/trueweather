package com.trueweather.data;

/**
 * Created by dimaMJ on 09.05.2016.
 */
public enum Forecast {
    RAIN, SUNNY, CLOUDY, STORM, SNOW;

    boolean isPrecipitation() {
        switch (this) {
            case RAIN:
            case STORM:
            case SNOW:
                return true;
            default:
                return false;
        }
    }
}
