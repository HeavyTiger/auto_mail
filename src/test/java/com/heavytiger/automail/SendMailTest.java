package com.heavytiger.automail;

import com.heavytiger.automail.pojo.MailContent;
import com.heavytiger.automail.service.ChpService;
import com.heavytiger.automail.service.PictureApi;
import com.heavytiger.automail.service.SendMail;
import com.heavytiger.automail.service.WeatherInform;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 * @author heavytiger
 * @version 1.0
 * @description 测试发送邮件
 * @date 2022/2/28 14:54
 */
@SpringBootTest
public class SendMailTest {

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

    /**
     * 测试发送普通邮件
     */
    @Test
    public void sendSimpleMail() {
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
            System.out.println("失败！");
        }
    }
}
