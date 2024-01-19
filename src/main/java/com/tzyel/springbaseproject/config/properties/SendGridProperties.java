package com.tzyel.springbaseproject.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("application.mail.sendgrid")
@Data
public class SendGridProperties {
    private String apiKey;
    private String fromEmail;
    private String fromName;
}
