package com.tongbao.log.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author xiaohaifang
 * @date 2019/7/10 11:30
 */
@Component
@ConfigurationProperties(prefix = "mail")
@Configuration
@Data
public class MailConfig {

    /*private static final String personal = MailConfig.personal;
    private static final String subject = MailConfig.subject;
    private static final String html = MailConfig.html;
    */
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String emailForm;
    private String emailTo;
    private String timeout;

    @Bean(name = "mailSender")
    public JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", timeout);
        p.setProperty("mail.smtp.auth", "false");
        sender.setJavaMailProperties(p);
        return sender;
    }
}
