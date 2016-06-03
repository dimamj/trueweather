package com.trueweather.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trueweather.data.Forecast;
import com.trueweather.data.WeatherDay;
import com.trueweather.data.WeatherSegment;
import com.trueweather.parsers.GismeteoParser;
import com.trueweather.parsers.MeteoParser;
import com.trueweather.parsers.ParserWeather;
import com.trueweather.parsers.SinoptikParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dimaMJ on 21.05.2016.
 */
@Service
public class WeatherService {

    private final List<ParserWeather> parsers = Collections.unmodifiableList(Lists.newArrayList(
            new SinoptikParser(),
            new GismeteoParser(),
            new MeteoParser()
    ));

    public List<WeatherDay> getTrueWeatherOnThreeDays(String city) {
        List<WeatherDay> resultFromParsers = Lists.newArrayList();
        parsers.stream().forEach(p -> {
            resultFromParsers.addAll(p.getWeatherOnThreeDays(city));
        });
        return getThreeDaysAvgWeather(resultFromParsers);
    }


    private List<WeatherDay> getThreeDaysAvgWeather(List<WeatherDay> resultFromParsers) {
        Map<LocalDate, WeatherDay> result = Maps.newHashMap();
        LocalDate date = LocalDate.now();
        for (int i = 0; i < 3; i++) {
            final LocalDate finalDate = date;
            List<WeatherDay> weatherDays = resultFromParsers.stream()
                    .filter(r -> r.getDate().equals(finalDate))
                    .collect(Collectors.toList());

            WeatherDay resDay = weatherDays.get(0);
            resDay.addVariants(resDay);

            for (int z = 1; z < weatherDays.size(); z++) {
                resDay.addVariants(weatherDays.get(z));
            }

            result.put(date, resDay);
            date = date.plusDays(1);
        }

        result.entrySet().stream().forEach(e -> e.getValue().calcAvgWeather());

        return result.values().stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());

    }

}
