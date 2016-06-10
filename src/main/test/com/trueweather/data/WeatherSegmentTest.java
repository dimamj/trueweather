package com.trueweather.data;

import com.google.common.collect.Lists;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import static com.trueweather.data.Forecast.*;
import static org.junit.Assert.*;

/**
 * Created by dimaMJ on 10.06.2016.
 */
public class WeatherSegmentTest {

    private WeatherSegment segment = new WeatherSegment();

    @Test
    public void getAvgForecastTest() {
        segment.getForecastVariants().addAll(Lists.newArrayList(
                RAIN, RAIN, SUNNY, CLOUDY
        ));

        assertEquals(RAIN, segment.getAvgForecast());

        segment.getForecastVariants().clear();
        segment.getForecastVariants().addAll(Lists.newArrayList(
                RAIN, RAIN, CLOUDY, CLOUDY
        ));

        assertEquals(RAIN, segment.getAvgForecast());

        segment.getForecastVariants().clear();
        segment.getForecastVariants().addAll(Lists.newArrayList(
                CLOUDY, CLOUDY, RAIN, RAIN
        ));

        assertEquals(RAIN, segment.getAvgForecast());

        segment.getForecastVariants().clear();
        segment.getForecastVariants().addAll(Lists.newArrayList(
                CLOUDY, SUNNY, STORM, RAIN
        ));

        assertEquals(RAIN, segment.getAvgForecast());

    }
}
