package com.mingyue.sendmsg.services;

import jakarta.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Random;

@Service
public class EmailService {

    public Logger logger = Logger.getLogger(this.getClass());

    @Value("${spring.mail.jndi-name}")
    private String sendName;

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private JavaMailSender mailSender;

    public void getCode(HttpServletRequest request) throws ServletRequestBindingException {

        String email = ServletRequestUtils.getRequiredStringParameter(request,"email");

        StringBuilder code = new StringBuilder();

        //获取httpSession
        HttpSession session = request.getSession();

        //拼接随机四个整数
        Random random = new Random();
        while (code.length() < 4) {
            code.append(random.nextInt(10));
        }

        Jedis jedis = jedisPool.getResource();
        // 有效期5分钟
        jedis.setex(session.getId(),(60L * 5),code.toString());

        logger.warn("sendName" + this.sendName);
        sendSimpleMail(email,this.sendName,"明月的小屋-验证码","您的验证码：" + code.toString() + "，有效期为5分钟");

    }

    public void sendSimpleMail(String to,String sendName,String subject,String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发件人
        message.setFrom(sendName);
        //邮件收件人 1或多个
        message.setTo(to.split(","));
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        checkMail(message);
        //邮件发送时间
        message.setSentDate(new Date());


        mailSender.send(message);

    }


    public void checkMail(SimpleMailMessage mailRequest) {
        Assert.notNull(mailRequest,"邮件请求不能为空");
        Assert.notNull(mailRequest.getTo(), "邮件收件人不能为空");
        Assert.notNull(mailRequest.getSubject(), "邮件主题不能为空");
        Assert.notNull(mailRequest.getText(), "邮件收件人不能为空");
    }


}
