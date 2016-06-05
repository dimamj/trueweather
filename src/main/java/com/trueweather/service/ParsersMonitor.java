package com.trueweather.service;

import com.google.common.collect.Lists;
import com.trueweather.data.WeatherDay;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dimaMJ on 05.06.2016.
 */
@Aspect
@Component
public class ParsersMonitor {

    private Logger logger = LoggerFactory.getLogger("parsers");

    @Around("execution(* com.trueweather.parsers.*.getWeatherOnThreeDays(..))")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        List<WeatherDay> result = Lists.newArrayList();
        try {
            return pjp.proceed();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return result;
        }
    }
}
