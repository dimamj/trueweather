package com.trueweather.utils;

import com.google.common.collect.Lists;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dimaMJ on 06.06.2016.
 */
public class CookieUtils {

    private static final String COOKIE_NAME = "true-weather-user";

    public static void addCityToCookie(HttpServletRequest request, HttpServletResponse response, String city) {
        Cookie resultCookie = getCookie(request);
        city = encodeCity(city);
        if (resultCookie == null) {
            resultCookie = new Cookie(COOKIE_NAME, city);
            resultCookie.setMaxAge(60 * 60 * 24 * 150);
            resultCookie.setPath("/");
        } else {
            String[] cities = resultCookie.getValue().split(",");
            List<String> list = Lists.newArrayList(cities);
            if (list.contains(city)) {
                list.remove(city);
                list.add(0, city);
            } else {
                list.add(0, city);
            }

            if (list.size() == 5) {
                list.remove(4);
            }

            resultCookie.setValue(list.stream().collect(Collectors.joining(",")));

        }
        response.addCookie(resultCookie);
    }

    public static String[] getLastCitiesFromCookie(HttpServletRequest request) {
        Cookie resultCookie = getCookie(request);
        if (resultCookie != null) {
            String val = decodeCity(resultCookie.getValue());
            return val.split(",");
        } else {
            return null;
        }
    }


    private static Cookie getCookie(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(COOKIE_NAME)) {
                return cookie;
            }
        }
        return null;
    }

    private static String encodeCity(String val) {
        try {
            return URLEncoder.encode(val, "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String decodeCity(String val) {
        try {
            return URLDecoder.decode(val, "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
