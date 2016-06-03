package com.trueweather.parsers;

import com.trueweather.data.WeatherDay;

import java.util.List;

/**
 * Created by dimaMJ on 09.05.2016.
 */
public interface ParserWeather {
    List<WeatherDay> getWeatherOnThreeDays(String cityName);
}
