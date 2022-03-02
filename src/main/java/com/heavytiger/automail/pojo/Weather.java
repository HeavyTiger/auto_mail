package com.heavytiger.automail.pojo;

/**
 * @author heavytiger
 * @version 1.0
 * @description 天气类
 * @date 2022/2/28 21:06
 */
public class Weather {
    private String text_day;
    private String text_night;
    private Integer low;
    private Integer high;

    public Weather() {
    }

    public Weather(String text_day, String text_night, Integer low, Integer high) {
        this.text_day = text_day;
        this.text_night = text_night;
        this.low = low;
        this.high = high;
    }

    public String getText_day() {
        return text_day;
    }

    public void setText_day(String text_day) {
        this.text_day = text_day;
    }

    public String getText_night() {
        return text_night;
    }

    public void setText_night(String text_night) {
        this.text_night = text_night;
    }

    public Integer getLow() {
        return low;
    }

    public void setLow(Integer low) {
        this.low = low;
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }
}
