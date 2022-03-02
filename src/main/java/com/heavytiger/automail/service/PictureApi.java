package com.heavytiger.automail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heavytiger.automail.pojo.MailContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * @author heavytiger
 * @version 1.0
 * @description 访问being每日一图，下载图片
 * @date 2022/2/28 18:33
 */
@Service
public class PictureApi {

    @Autowired
    private MailContent mailContent;

    public void getPictureInform() {
        StringBuffer strBuf = new StringBuffer();
        try {
            URL url = new URL("https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1");
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
        ObjectMapper objectMapper = new ObjectMapper();
        // json格式的String
        try {
            JsonNode jsonNode = objectMapper.readTree(strBuf.toString());
            // 设置图片标题
            String title = jsonNode.get("images").get(0).get("title").asText();
            System.out.println(title);
            mailContent.setTitle(title);
            // 获取原始地址
            String rawUrl ="https://www.bing.com" + jsonNode.get("images").get(0).get("url").asText();
            System.out.println(rawUrl);
            // 根据所在的机器设置图片存放位置
            String curDir = MailContent.IS_IN_LINUX ? MailContent.DIR_LINUX : MailContent.DIR_WIN;
            // 获取大图片地址
            int index = rawUrl.indexOf("&");
            String largeUrl = rawUrl.substring(0, index);
            System.out.println(largeUrl);
            mailContent.setLargePic(curDir + title + "_l.jpg");
            download(largeUrl, curDir + title + "_l.jpg");
            // 获取小图片地址
            String smallUrl = largeUrl.replaceAll("1920x1080", "800x480");
            System.out.println(smallUrl);
            mailContent.setSmallPic(curDir + title + "_s.jpg");
            download(smallUrl, curDir + title + "_s.jpg");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void download(String urlString, String filename){
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开链接
            URLConnection conn = url.openConnection();
            //设置请求超时为5s
            conn.setConnectTimeout(5 * 1000);
            // 1K的数据缓冲
            InputStream is = conn.getInputStream();
            byte[] bs = new byte[1024];
            int len;  // 读取到的数据长度

            // 输出的文件流
            File file = new File(filename);
            if(file.exists()) {
                // 如果文件存在则直接删除，避免重复写入文件
                file.delete();
            }
            FileOutputStream os = new FileOutputStream(file, true);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            System.out.println("下载失败！");
            e.printStackTrace();
        }
    }
}
