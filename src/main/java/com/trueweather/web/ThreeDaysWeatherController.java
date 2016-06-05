package com.trueweather.web;

import com.google.common.collect.Lists;
import com.trueweather.data.WeatherDay;
import com.trueweather.service.WeatherService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by dimaMJ on 14.05.2016.
 */
@Controller
public class ThreeDaysWeatherController {


    @Autowired
    private WeatherService weatherService;

    @RequestMapping(value = {"/", "/3days"}, method = RequestMethod.GET)
    public String threeDaysWeatherPage() {
        return "/3days";
    }

    @RequestMapping(value = "/search-weather", method = RequestMethod.GET)
    @ResponseBody
    public Result getThreeDaysWeather(String city) {
        if (city == null || city.isEmpty()) {
            return new Result("Город не задан!");
        }

        return new Result(weatherService.getTrueWeatherOnThreeDays(city.toLowerCase().trim()));
    }

    @Data
    public static class Result {
        private String error;
        private List<WeatherDay> result = Lists.newArrayList();

        public Result() {
        }

        public Result(String error) {
            this.error = error;
        }

        public Result(List<WeatherDay> result) {
            this.result = result;
        }
    }


}
