package com.trueweather;

import com.trueweather.data.WeatherSegment;
import com.trueweather.parsers.SinoptikParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.net.util.URLUtil;

import java.net.URLEncoder;

/**
 * Created by dimaMJ on 09.05.2016.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(Application.class, args);
    }

}
