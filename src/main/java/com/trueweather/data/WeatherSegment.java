package com.trueweather.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.util.Pair;
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

    public WeatherSegment() {
    }

    public WeatherSegment(int temperature) {
        this.temperature = temperature;
    }

    public WeatherSegment(int temperature, Forecast forecast) {
        this.temperature = temperature;
        this.forecast = forecast;
    }

    public void addVariants(WeatherSegment weatherSegment) {
        temperatureVariants.add(weatherSegment.getTemperature());
        forecastVariants.add(weatherSegment.getForecast());
    }


    public void calcAvgWeather() {
        temperature = (int) Math.ceil(temperatureVariants.stream().mapToInt((i) -> i).average().getAsDouble());
        forecast = getAvgForecast();
    }

    protected Forecast getAvgForecast() {
        Map<Forecast, Integer> map = Maps.newHashMap();

        final Map<Forecast, Integer> finalMap = map;
        forecastVariants.stream()
                .forEach(f -> {
                    Integer count = finalMap.getOrDefault(f, 0);
                    map.put(f, ++count);
                });

        //если попадаются с одинаковым кол-вом очков то приоритетней будут осадки
       return map.entrySet().stream()
               .sorted((a, b) -> {
                   if (a.getValue().equals(b.getValue())) {
                       return b.getKey().isPrecipitation() ? 1 : a.getKey().isPrecipitation() ? -1 : 0;
                   }
                   return b.getValue().compareTo(a.getValue());
               }).limit(1).map(Map.Entry::getKey).findFirst().get();
    }

}
