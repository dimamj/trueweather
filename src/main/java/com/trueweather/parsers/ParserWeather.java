package com.trueweather.parsers;

import com.trueweather.data.WeatherDay;
import com.trueweather.data.WeatherFromParser;
import com.trueweather.data.WeatherSegment;

import java.util.List;

/**
 * Created by dimaMJ on 09.05.2016.
 */
public interface ParserWeather {
    WeatherFromParser getWeather(String cityName);
}
