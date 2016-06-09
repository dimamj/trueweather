package com.trueweather.parsers;

import com.google.common.collect.Lists;
import com.trueweather.data.WeatherDay;
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
public class MeteoParser implements ParserWeather {

    private static final Site site = Site.METEO;

    @Override
    public List<WeatherDay> getWeatherOnThreeDays(String cityName) {
        String resultUrl = UrlWeatherUtils.buildUrl(cityName, site);
        if(resultUrl.contains("month")){
            resultUrl = resultUrl.substring(0,resultUrl.length()-6);
        }
        List<WeatherDay> result = Lists.newArrayList();
        LocalDate date = LocalDate.now();
        Connection connection = Jsoup.connect(resultUrl).userAgent("Chrome 41.0.2228.0");
        Element body;
        try {
            body = connection.get().body();
        } catch (IOException e) {
            throw new RuntimeException(site.getURL(), e);
        }
        Elements elements = body.select("tr.wnow_head");

        for (int i = 0; i < 3; i++) {
            result.add(getWeatherDay(elements.get(i), date));
            date = date.plusDays(1);
        }

        return result;
    }


    private WeatherDay getWeatherDay(Element element, LocalDate date) {
        WeatherDay weatherDay = new WeatherDay(date,UrlWeatherUtils.Site.METEO);
        List<WeatherSegment> weatherSegments = Lists.newLinkedList();

        Elements temperatures = element.select("td > div > div.wnow_tmpr");
        Iterator<Element> iterator = temperatures.iterator();

        while (iterator.hasNext()) {
            int temp = 0;
            TextNode node = (TextNode) iterator.next().childNode(0);
            String val =  node.text().trim();
            val = val.substring(0, val.length() - 1);
            temp += Integer.valueOf(val);
            weatherSegments.add(new WeatherSegment(temp));
        }

        Elements forecasts = element.select("td > div > div.wnow_icns");
        iterator = forecasts.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            Element img =  iterator.next().select("img").first();
            String val = img.attr("title");
            weatherSegments.get(index++).setForecast(
                    WeatherUtils.getForecastFromText(val));
        }

        weatherDay.setWeatherDaySegments(weatherSegments);
        return weatherDay;
    }
}
