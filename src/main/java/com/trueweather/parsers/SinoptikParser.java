package com.trueweather.parsers;

import com.google.common.collect.Lists;
import com.trueweather.data.Forecast;
import com.trueweather.data.WeatherDay;
import com.trueweather.data.WeatherFromParser;
import com.trueweather.data.WeatherSegment;
import com.trueweather.utils.UrlWeatherUtils;
import com.trueweather.utils.UrlWeatherUtils.Site;
import com.trueweather.utils.WeatherUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by dimaMJ on 09.05.2016.
 */
@Component
public class SinoptikParser implements ParserWeather {

    private static final Site site = Site.SINOPTIK;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public WeatherFromParser getWeather(String cityName) {
        String resultUrl = UrlWeatherUtils.buildUrl(cityName, site);
        List<WeatherDay> result = Lists.newArrayList();
        LocalDate date = LocalDate.now();

        for (int i = 0; i < 3; i++) {
            Connection connection = Jsoup.connect(resultUrl + "/" + date.format(formatter)).userAgent("Chrome 41.0.2228.0");
            try {
                Element body = connection.get();
                result.add(getWeatherDay(body.select("table.weatherDetails").first(), date, i == 2 && LocalDateTime.now()
                        .isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0)))));
            } catch (IOException e) {
                throw new RuntimeException(site.getURL(), e);
            }

            date = date.plusDays(1);
        }

        Connection connection = Jsoup.connect(resultUrl + "/" + LocalDate.now().format(formatter)).userAgent("Chrome 41.0.2228.0");
        WeatherSegment currentWeather;
        try {
            Element body = connection.get();
            currentWeather = getCurrentWeather(body);
        } catch (IOException e) {
            throw new RuntimeException(site.getURL(), e);
        }

        return new WeatherFromParser(currentWeather, result);
    }


    private WeatherSegment getCurrentWeather(Element element) {
        Element tempEl = element.select(".today-temp").first();
        String tempStr = ((TextNode) tempEl.childNode(0)).text();
        int temp = Integer.parseInt(tempStr.substring(0, tempStr.length() - 2));

        Element forecastEl = element.select(".imgBlock").select("img").first();
        Forecast forecast = WeatherUtils.getForecastFromText(forecastEl.attr("alt"));
        return new WeatherSegment(temp, forecast);
    }


    private WeatherDay getWeatherDay(Element element, LocalDate date, boolean thirdDay) {
        WeatherDay weatherDay = new WeatherDay(date, UrlWeatherUtils.Site.SINOPTIK);

        List<WeatherSegment> weatherSegments = Lists.newLinkedList();

        Elements temperatures = element.select("tr.temperature > td");

        Iterator<Element> iterator = temperatures.iterator();

        int temp = 0;
        for (int i = 1; iterator.hasNext(); i++) {
            TextNode node = (TextNode) iterator.next().childNode(0);
            String val = node.text().substring(0, node.text().length() - 1);
            temp += Integer.valueOf(val);
            if (i % 2 == 0 && !thirdDay) {
                weatherSegments.add(new WeatherSegment((int) Math.ceil((double) temp / 2)));
                temp = 0;
            } else if (thirdDay) {
                weatherSegments.add(new WeatherSegment(temp));
                temp = 0;
            }
        }

        Elements forecasts = element.select("tr.weatherIcoS > td");
        iterator = forecasts.iterator();
        List<String> forecastList = Lists.newArrayList();

        int index = 0;
        for (int i = 1; iterator.hasNext(); i++) {
            Element div = iterator.next().select("div").first();
            forecastList.add(div.attr("title"));

            if (i % 2 == 0 && !thirdDay) {
                weatherSegments.get(index++).setForecast(
                        WeatherUtils.getForecastFromText(
                                getPriorityForecast(forecastList.get(0), forecastList.get(1))));
                forecastList.clear();
            } else if (thirdDay) {
                weatherSegments.get(index++).setForecast(
                        WeatherUtils.getForecastFromText(forecastList.get(0)));
                forecastList.clear();
            }
        }

        weatherDay.setWeatherDaySegments(weatherSegments);
        return weatherDay;
    }

    //осадки будут приоритетные при выборе между 2-мя ячейками
    //а вообще лучше бы еще извлекать для синоптика вер-ть и по ней смотреть
    //если осадки и в-ять > 50 то возвращать
    private String getPriorityForecast(String f, String s) {
        for (String w : WeatherUtils.getForecastsPriority()) {
            if (f.contains(w)) {
                return f;
            } else if (s.contains(w)) {
                return s;
            }
        }

        return f;
    }


}
