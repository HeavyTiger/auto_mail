package com.heavytiger.automail.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author heavytiger
 * @version 1.0
 * @description TODO
 * @date 2022/2/28 17:43
 */
@SpringBootTest
class WeatherInformTest {

    @Test
    void getWeatherInform() {
        WeatherInform weatherInform = new WeatherInform();
        weatherInform.getWeatherInform();
    }
}