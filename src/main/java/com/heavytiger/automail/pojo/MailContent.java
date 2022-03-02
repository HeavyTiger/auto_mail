package com.heavytiger.automail.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author heavytiger
 * @version 1.0
 * @description 邮件中的动态内容
 * @date 2022/2/28 22:19
 */
@Component
public class MailContent {
    // 是否在Linux系统下

    public static boolean IS_IN_LINUX;
    // Win下的图片存放路径
    public static String DIR_WIN;
    // Linux下的图片存放路径
    public static String DIR_LINUX;
    // 牵手时间
    public static Date FIRST_DATE;
    // 当前累计时间
    private Long curTotalDays;
    // 当前温度
    private Integer curTemp;
    // 当前湿度
    private Integer curHumidity;
    // 存储预测天气
    private Weather[] weathers = new Weather[3];
    // 当前小图片路径 800 * 480
    private String smallPic;
    // 当前大图片路径 1920 * 1080
    private String largePic;
    // 当前时间
    private String date;
    // 当前图片标题
    private String title;
    // 当前数据库文案
    private String sentence;

    @Value("${config.in-linux}")
    public void setIsInLinux(boolean inLinux) {
        IS_IN_LINUX = inLinux;
    }

    @Value("${config.dir-win}")
    public void setDirWin(String dirWin) {
        DIR_WIN = dirWin;
    }

    @Value("${config.dir-linux}")
    public void setDirLinux(String dirLinux) {
        DIR_LINUX = dirLinux;
    }

    @Value("${config.first-date}")
    public void setFirstDate(Long firstDate) {
        FIRST_DATE = new Date(firstDate);
    }

    public void resolveDate() {
        Date curDate = new Date();
        this.setDate(new SimpleDateFormat("yyyy / MM / dd").format(curDate));
        this.setCurTotalDays((curDate.getTime() - FIRST_DATE.getTime()) / 86400000 + 1);
    }

    public Object[] transObjects() {
        // 将所有的内容转为Objects[]对象
        Object[] objects = new Object[18];
        objects[0] = curTotalDays;
        objects[1] = curTemp;
        objects[2] = curHumidity;
        for(int i = 0; i < 3; i++) {
            objects[i * 4 + 3] = weathers[i].getText_day();
            objects[i * 4 + 4] = weathers[i].getText_night();
            objects[i * 4 + 5] = weathers[i].getHigh();
            objects[i * 4 + 6] = weathers[i].getLow();
        }
        objects[15] = date;
        objects[16] = title == null ? "美图" : title;
        objects[17] = sentence == null ? "Hello world!" : sentence;
        return objects;
    }

    public MailContent() {
    }

    public MailContent(Long curTotalDays, Integer curTemp, Integer curHumidity, Weather[] weathers, String smallPic,
                       String largePic, String date, String title, String sentence) {
        this.curTotalDays = curTotalDays;
        this.curTemp = curTemp;
        this.curHumidity = curHumidity;
        this.weathers = weathers;
        this.smallPic = smallPic;
        this.largePic = largePic;
        this.date = date;
        this.title = title;
        this.sentence = sentence;
    }

    public Long getCurTotalDays() {
        return curTotalDays;
    }

    public void setCurTotalDays(Long curTotalDays) {
        this.curTotalDays = curTotalDays;
    }

    public Integer getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(Integer curTemp) {
        this.curTemp = curTemp;
    }

    public Integer getCurHumidity() {
        return curHumidity;
    }

    public void setCurHumidity(Integer curHumidity) {
        this.curHumidity = curHumidity;
    }

    public Weather[] getWeathers() {
        return weathers;
    }

    public void setWeathers(Weather[] weathers) {
        this.weathers = weathers;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getLargePic() {
        return largePic;
    }

    public void setLargePic(String largePic) {
        this.largePic = largePic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
