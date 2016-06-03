package com.trueweather.utils;

import com.google.common.collect.Maps;
import com.trueweather.data.Forecast;

import java.util.Map;
import java.util.Optional;

/**
 * Created by dimaMJ on 21.05.2016.
 */
public class WeatherUtils {

    private static final Map<String, Forecast> forecasts = Maps.newLinkedHashMap();

    static {
        forecasts.put("гроз", Forecast.STORM);
        forecasts.put("дожд|ливни", Forecast.RAIN);
        forecasts.put("снег", Forecast.SNOW);
        forecasts.put("облачн|пасмурно|туман", Forecast.CLOUDY);
        forecasts.put("ясно", Forecast.SUNNY);
    }

    public static Forecast getForecastFromText(String text) {
        Optional<Map.Entry<String, Forecast>> optional = forecasts.entrySet().stream()
                .filter(e -> checkContains(text.toLowerCase(), e.getKey()))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get().getValue();
        } else {
            throw new RuntimeException("forecast not found : " + text);
        }
    }

    private static boolean checkContains(String text, String key) {
        String[] split = key.split("\\|");
        for (String s : split) {
            if (text.contains(s)) {
                return true;
            }
        }
        return false;
    }
}
