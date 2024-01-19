package com.tzyel.springbaseproject.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("application.mail.smtp")
@Data
public class SmtpProperties {
    private String host;
    private String port;
    private String username;
    private String password;
    private String sender;
    private String sslEnable;
}
