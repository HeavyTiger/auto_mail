package com.heavytiger.automail.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author heavytiger
 * @version 1.0
 * @description 生成百度API的sn码
 * @date 2022/2/28 17:17
 */
public class SNUtils {

    // 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
    public static String getMD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 对Map内所有value作utf8编码，拼接返回结果
    public static String toQueryString(Map<String, String> data) {
        StringBuffer queryString = new StringBuffer();
        for (Map.Entry<String, String> pair : data.entrySet()) {
            queryString.append(pair.getKey())
                    .append("=")
                    .append(URLEncoder.encode((String) pair.getValue(), StandardCharsets.UTF_8))
                    .append("&");
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    public static String getAPI(String district_id) {

        // 计算sn跟参数对出现顺序有关，get请求请使用LinkedHashMap保存<key,value>，该方法根据key的插入顺序排序;
        Map<String, String> paramsMap = new LinkedHashMap<>();
        paramsMap.put("district_id", district_id);
        paramsMap.put("data_type", "all");
        paramsMap.put("output", "json");
        paramsMap.put("ak", "your_ak");

        // 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，拼接返回结果
        String paramsStr = SNUtils.toQueryString(paramsMap);

        // 对paramsStr前面拼接上/weather/v1/?，后面直接拼接yourSK得到/weather/v1/?xxxx
        String wholeStr = new String("/weather/v1/?" + paramsStr + "your_sn");

        // 对上面wholeStr再作utf8编码
        String tempStr = URLEncoder.encode(wholeStr, StandardCharsets.UTF_8);

        // 调用下面的MD5方法得到最后的sn签名
        String finalStr = "https://api.map.baidu.com/weather/v1/?" + paramsStr + "&sn=" + SNUtils.getMD5(tempStr);
        System.out.println(finalStr);
        return finalStr;
    }
}
