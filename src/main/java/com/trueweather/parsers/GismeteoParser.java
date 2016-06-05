package com.trueweather.parsers;

import com.google.common.collect.Lists;
import com.trueweather.data.WeatherDay;
import com.trueweather.data.WeatherSegment;
import com.trueweather.utils.UrlWeatherUtils;
import com.trueweather.utils.WeatherUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dimaMJ on 21.05.2016.
 */
@Component
public class GismeteoParser implements ParserWeather {

    private static final String URL = "https://www.gismeteo.ua/";

    @Override
    public List<WeatherDay> getWeatherOnThreeDays(String cityName) {
        String resultUrl = UrlWeatherUtils.buildUrl(URL, cityName, UrlWeatherUtils.Site.GISMETEO);
        List<WeatherDay> result = Lists.newArrayList();
        LocalDate date = LocalDate.now();

        Connection connection = Jsoup.connect(resultUrl).userAgent("Chrome 41.0.2228.0");
        for (int i = 1; i < 4; i++) {
            try {
                Element body = connection.get().body();
                result.add(getWeatherDay(body.select("tbody#tbwdaily" + i).first(), date));
            } catch (IOException e) {
                throw new RuntimeException(URL, e);
            }

            date = date.plusDays(1);
        }

        return result;
    }


    private WeatherDay getWeatherDay(Element element, LocalDate date) {
        WeatherDay weatherDay = new WeatherDay(date,UrlWeatherUtils.Site.GISMETEO);
        List<WeatherSegment> weatherSegments = Lists.newLinkedList();

        Elements temperatures = element.select("tr > td.temp");
        Iterator<Element> iterator = temperatures.iterator();

        while (iterator.hasNext()) {
            int temp = 0;
            TextNode node = (TextNode) iterator.next().childNode(0).childNode(0);
            String val = node.text();
            temp += Integer.valueOf(val);
            weatherSegments.add(new WeatherSegment(temp));
        }

        Elements forecasts = element.select("tr > td.cltext");
        iterator = forecasts.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            TextNode node = (TextNode) iterator.next().childNode(0);
            String val = node.text();
            weatherSegments.get(index++).setForecast(
                    WeatherUtils.getForecastFromText(val));
        }

        weatherDay.setWeatherDaySegments(weatherSegments);
        System.out.println(URL + " : " + weatherDay.getWeatherDaySegments().size());
        return weatherDay;
    }
}
