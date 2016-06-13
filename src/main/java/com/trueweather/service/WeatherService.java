package com.trueweather.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trueweather.data.WeatherDay;
import com.trueweather.data.WeatherFromParser;
import com.trueweather.data.WeatherSegment;
import com.trueweather.parsers.ParserWeather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    @Autowired
    private ParserWeather gismeteoParser, meteoParser, sinoptikParser, yandexParser;

    private List<ParserWeather> parsers;

    @PostConstruct
    private void init() {
        parsers = Collections.unmodifiableList(Lists.newArrayList(
                sinoptikParser,
                gismeteoParser,
                meteoParser,
                yandexParser
        ));
    }


    public WeatherFromParser getTrueWeatherOnThreeDays(String city) {
        List<WeatherFromParser> resultFromParsers = Lists.newArrayList();
        parsers.stream().forEach(p -> {
            resultFromParsers.add(p.getWeather(city));
        });
        return getThreeDaysAvgWeather(resultFromParsers);
    }


    private WeatherFromParser getThreeDaysAvgWeather(List<WeatherFromParser> resultFromParsers) {
        Map<LocalDate, WeatherDay> result = Maps.newHashMap();
        List<WeatherDay> allWeather = Lists.newArrayList();
        resultFromParsers.forEach(r -> allWeather.addAll(r.getWeatherOnThreeDays()));
        LocalDate date = LocalDate.now();
        if (resultFromParsers.isEmpty()) {
            return new WeatherFromParser();
        }

        for (int i = 0; i < 3; i++) {
            final LocalDate finalDate = date;
            List<WeatherDay> weatherDays = allWeather.stream()
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

        List<WeatherDay> threeDaysWeather =  result.values().stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());

        WeatherSegment currentWeather = new WeatherSegment();

        resultFromParsers.forEach(r->{
            currentWeather.addVariants(r.getCurrentWeather());
        });

        currentWeather.calcAvgWeather();

        return new WeatherFromParser(currentWeather,threeDaysWeather);

    }

}
