package com.trueweather.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dimaMJ on 09.05.2016.
 */
@Data
public class WeatherSegment {
    private int temperature;
    private Forecast forecast;
    private List<Integer> temperatureVariants = Lists.newArrayList();
    private List<Forecast> forecastVariants = Lists.newArrayList();

    public WeatherSegment(int temperature) {
        this.temperature = temperature;
    }

    public void addVariants(WeatherSegment weatherSegment) {
        temperatureVariants.add(weatherSegment.getTemperature());
        forecastVariants.add(weatherSegment.getForecast());
    }


    public void calcAvgWeather() {
        temperature = (int) Math.ceil(temperatureVariants.stream().mapToInt((i) -> i).average().getAsDouble());
        forecast = getAvgForecast();
    }

    private Forecast getAvgForecast() {
        Map<Forecast, Integer> map = Maps.newHashMap();

        final Map<Forecast, Integer> finalMap = map;
        forecastVariants.stream()
                .forEach(f -> {
                    Integer count = finalMap.getOrDefault(f,0);
                    map.put(f,++count);
                });

        //TODO нужно усовершенствовать, т.е. брать 2 последних и проверять если оценка одинакова то выбирать дождь, если есть
        //иначе любой вариант
        return map.entrySet().stream()
                .sorted((a,b)->b.getValue().compareTo(a.getValue()))
                .limit(1)
                .map(Map.Entry::getKey)
                .findFirst().get();
    }

}
