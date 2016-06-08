package com.trueweather.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by dimaMJ on 14.05.2016.
 */
public class UrlWeatherUtils {

    public static String buildUrl(String url, String cityName, Site site) {
        switch (site) {
            case SINOPTIK:
                return getUrlFromGoogleSearch(cityName, "sinoptik").replaceAll("25","");
            case GISMETEO:
                return getUrlFromGoogleSearch(cityName, "gismeteo");
            case METEO:
                return getUrlFromGoogleSearch(cityName, "meteoua неделя");
            default:
                throw new RuntimeException("Unknown site.");
        }
    }

    private static String getUrlFromGoogleSearch(String cityName, String site) {
        String url = "https://www.google.ru/search?q=";
        String q = site + " " + cityName;
        try {
            Document document = Jsoup.connect(url + URLEncoder.encode(q, "UTF8")).userAgent("Chrome 41.0.2228.0").get();
            Element element = document.select("h3.r > a").first();
            String href = element.attr("href");
            href = href.substring(href.indexOf("q=") + 2, href.indexOf("&sa"));
            return href;
        } catch (Exception e) {
            throw new RuntimeException(q, e);
        }
    }

    public enum Site {
        SINOPTIK, GISMETEO, METEO
    }

}
