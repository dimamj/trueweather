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
 * Created by dimaMJ on 09.06.2016.
 */
@Component
public class YandexParser implements ParserWeather {

    private static final Site site = Site.YANDEX;

    @Override
    public WeatherFromParser getWeather(String cityName) {
        String resultUrl = UrlWeatherUtils.buildUrl(cityName, site);

        List<WeatherDay> result = Lists.newArrayList();
        LocalDate date = LocalDate.now();
        Connection connection = Jsoup.connect(resultUrl + "/details").userAgent("Chrome 41.0.2228.0");
        Element body;
        try {
            body = connection.get().body();
        } catch (IOException e) {
            throw new RuntimeException(site.getURL(), e);
        }
        Elements elements = body.select("dd.forecast-detailed__day-info > table.weather-table");

        for (int i = 0; i < 3; i++) {
            result.add(getWeatherDay(elements.get(i), date));
            date = date.plusDays(1);
        }

        return new WeatherFromParser(getCurrentWeather(body), result);
    }

    private WeatherSegment getCurrentWeather(Element element) {
        Element block = element.select(".current-weather__col_type_now").first();

        Element tempEl = block.select(".current-weather__thermometer").first();
        String tempStr = ((TextNode) tempEl.childNode(0)).text();
        int temp = Integer.parseInt(tempStr.substring(0, tempStr.length() - 3));

        Element forecastEl = block.select(".current-weather__comment").first();
        Forecast forecast = WeatherUtils.getForecastFromText((
                (TextNode) forecastEl.childNode(0)).text());
        return new WeatherSegment(temp, forecast);
    }

    private WeatherDay getWeatherDay(Element element, LocalDate date) {
        WeatherDay weatherDay = new WeatherDay(date, Site.YANDEX);
        List<WeatherSegment> weatherSegments = Lists.newLinkedList();

        Elements temperatures = element.select("div.weather-table__temp");
        Iterator<Element> iterator = temperatures.iterator();

        while (iterator.hasNext()) {
            int temp = 0;
            TextNode node = (TextNode) iterator.next().childNode(0);
            String val = node.text().trim();
            String[] temps = val.split("…");
            if (temps.length == 2) {
                temp = Integer.valueOf(temps[0]) + Integer.valueOf(temps[1]);
                temp = (int) Math.ceil((double) temp / 2);
            } else {
                temp = Integer.valueOf(temps[0]);
            }
            weatherSegments.add(new WeatherSegment(temp));
        }

        Elements forecasts = element.select("td.weather-table__body-cell_type_condition > div.weather-table__value");
        iterator = forecasts.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            TextNode node = (TextNode) iterator.next().childNode(0);
            weatherSegments.get(index++).setForecast(
                    WeatherUtils.getForecastFromText(node.text()));
        }

        weatherDay.setWeatherDaySegments(weatherSegments);
        return weatherDay;
    }
}
