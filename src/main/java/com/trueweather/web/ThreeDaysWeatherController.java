package com.trueweather.web;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.trueweather.data.WeatherDay;
import com.trueweather.service.WeatherService;
import com.trueweather.utils.CookieUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by dimaMJ on 14.05.2016.
 */
@Controller
public class ThreeDaysWeatherController {

    @Autowired
    private WeatherService weatherService;
    private Gson gson = new Gson();

    @RequestMapping(value = {"/", "/3days"}, method = RequestMethod.GET)
    public String threeDaysWeatherPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        String[] cities = CookieUtils.getLastCitiesFromCookie(request);
        if (cities != null) {
            map.put("cities", gson.toJson(cities));
        }
        return "/3days";
    }

    @RequestMapping(value = "/search-weather", method = RequestMethod.GET)
    @ResponseBody
    public Result getThreeDaysWeather(HttpServletRequest request, HttpServletResponse response, String city) {
        if (city == null || city.isEmpty()) {
            return new Result("Город не задан!");
        }
        CookieUtils.addCityToCookie(request, response, city.toLowerCase().trim());
        return new Result(weatherService.getTrueWeatherOnThreeDays(city.toLowerCase().trim()),
               CookieUtils.getLastCitiesFromCookie(request));
    }

    @Data
    public static class Result {
        private String error;
        private List<WeatherDay> result = Lists.newArrayList();
        private String[] cities;

        public Result() {
        }

        public Result(String error) {
            this.error = error;
        }

        public Result(List<WeatherDay> result, String[] cities) {
            this.result = result;
            this.cities = cities;
        }
    }


}
