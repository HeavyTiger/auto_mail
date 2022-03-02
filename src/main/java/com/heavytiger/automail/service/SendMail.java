package com.heavytiger.automail.service;

import com.heavytiger.automail.pojo.MailContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 * @author heavytiger
 * @version 1.0
 * @description 发送邮件
 * @date 2022/2/28 14:50
 */
@Service
public class SendMail {

    @Autowired
    private MailContent mailContent;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private WeatherInform weatherInform;

    @Autowired
    private PictureApi pictureApi;

    @Autowired
    private ChpService chpService;

    @Scheduled(cron = "0 0 7 * * ?")
    public void sendMailWithContent() {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper= null;
        mailContent.resolveDate();
        weatherInform.getWeatherInform();
        pictureApi.getPictureInform();
        File picSmall = new File(mailContent.getSmallPic());
        File picLarge = new File(mailContent.getLargePic());
        mailContent.setSentence(chpService.getSentence());
        try {
            InputStream text =  SendMail.class.getClassLoader().getResourceAsStream("html/template.html");
            byte[] buffer = new byte[text.available()];
            text.read(buffer);
            String content = new String(buffer);
            text.close();
            content = MessageFormat.format(content, mailContent.transObjects());
            helper = new MimeMessageHelper(mimeMessage,true, "UTF-8");
            helper.setFrom(new InternetAddress("heavytiger<462857080@qq.com>"));
            helper.setTo("hannachen12138@gmail.com");
            helper.setBcc("chengmiaodeng@gmail.com");
            helper.setSubject("一封暖暖的小邮件");
            helper.setText(content, true);
            // 图片占位写法  如果图片链接写入模板 注释下面这一行
            helper.addInline("pic",new FileSystemResource(picSmall));
            helper.addAttachment(mailContent.getTitle() + ".jpg", picLarge);
            javaMailSender.send(mimeMessage);
            System.out.println("邮件已经发送！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("邮件发送失败！");
        }
    }
}
