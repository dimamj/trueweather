package com.trueweather.data;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * Created by dimaMJ on 13.06.2016.
 */
@Data
public class WeatherFromParser {
    WeatherSegment currentWeather;
    List<WeatherDay> weatherOnThreeDays = Lists.newArrayList();

    public WeatherFromParser() {
    }

    public WeatherFromParser(WeatherSegment currentWeather, List<WeatherDay> weatherOnThreeDays) {
        this.currentWeather = currentWeather;
        this.weatherOnThreeDays = weatherOnThreeDays;
    }
}
