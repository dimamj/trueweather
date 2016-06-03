package com.trueweather.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.trueweather.utils.UrlWeatherUtils;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by dimaMJ on 14.05.2016.
 */
@Data
public class WeatherDay {
    @JsonFormat(pattern="dd MMMM")
    private LocalDate date;
    private List<WeatherSegment> weatherDaySegments = Lists.newLinkedList();
    private UrlWeatherUtils.Site fromSite;

    public WeatherDay() {
    }

    public WeatherDay(LocalDate date, UrlWeatherUtils.Site site) {
        this.date = date;
        this.fromSite = site;
    }

    public void addVariants(WeatherDay other) {
        for (int i = 0; i < weatherDaySegments.size(); i++) {
            weatherDaySegments.get(i).addVariants(other.getWeatherDaySegments().get(i));
        }
    }

    public void calcAvgWeather() {
        fromSite = null; //т.к. после этой стадии уже инфа будет не нужной
        weatherDaySegments.stream()
                .forEach(WeatherSegment::calcAvgWeather);
    }
}
