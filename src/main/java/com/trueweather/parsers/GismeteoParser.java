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
import java.util.Iterator;
import java.util.List;

/**
 * Created by dimaMJ on 21.05.2016.
 */
@Component
public class GismeteoParser implements ParserWeather {

    private static final Site site = Site.GISMETEO;

    @Override
    public WeatherFromParser getWeather(String cityName) {
        String resultUrl = UrlWeatherUtils.buildUrl(cityName, site);
        List<WeatherDay> result = Lists.newArrayList();
        LocalDate date = LocalDate.now();

        Connection connection = Jsoup.connect(resultUrl).userAgent("Chrome 41.0.2228.0");
        Element body;
        try {
            body = connection.get().body();
            for (int i = 1; i < 4; i++) {
                result.add(getWeatherDay(body.select("tbody#tbwdaily" + i).first(), date));
                date = date.plusDays(1);
            }
        } catch (IOException e) {
            throw new RuntimeException(site.getURL(), e);
        }

        return new WeatherFromParser(getCurrentWeather(body), result);
    }

    private WeatherSegment getCurrentWeather(Element element) {
        Element block = element.select("div.higher").first();
        Element tempEl = block.select(".value.m_temp.c").first();
        TextNode node = (TextNode) tempEl.childNode(0);
        int temp = Integer.parseInt(node.text());

        Element forecastEl = block.select(".cloudness").select("td").first();
        Forecast forecast = WeatherUtils.getForecastFromText((
                (TextNode) forecastEl.childNode(0)).text());
        return new WeatherSegment(temp, forecast);
    }

    private WeatherDay getWeatherDay(Element element, LocalDate date) {
        WeatherDay weatherDay = new WeatherDay(date, UrlWeatherUtils.Site.GISMETEO);
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
        return weatherDay;
    }
}
