package com.heavytiger.automail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heavytiger.automail.pojo.MailContent;
import com.heavytiger.automail.pojo.Weather;
import com.heavytiger.automail.utils.SNUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * @author heavytiger
 * @version 1.0
 * @description 获取当日的天气情况
 * @date 2022/2/28 16:50
 */
@Service
public class WeatherInform {

    @Autowired
    private MailContent mailContent;

    public void getWeatherInform() {
        // 调用百度天气API
        String weatherUrl = SNUtils.getAPI("310104");
        StringBuffer strBuf = new StringBuffer();
        try {
            URL url = new URL(weatherUrl);
            URLConnection conn = url.openConnection();
            // 转为UTF-8编码
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = reader.readLine()) != null)
                strBuf.append(line).append(" ");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // json格式的String
        // return strBuf.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(strBuf.toString());
            mailContent.setCurTemp(jsonNode.get("result").get("now").get("temp").asInt());
            mailContent.setCurHumidity(jsonNode.get("result").get("now").get("rh").asInt());
            Weather[] weathers = new Weather[3];
            for(int i = 0; i < 3; i++) {
                weathers[i] = new Weather();
                weathers[i].setText_day(jsonNode.get("result").get("forecasts").get(i).get("text_day").asText());
                weathers[i].setText_night(jsonNode.get("result").get("forecasts").get(i).get("text_night").asText());
                weathers[i].setHigh(jsonNode.get("result").get("forecasts").get(i).get("high").asInt());
                weathers[i].setLow(jsonNode.get("result").get("forecasts").get(i).get("low").asInt());
            }
            mailContent.setWeathers(weathers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
