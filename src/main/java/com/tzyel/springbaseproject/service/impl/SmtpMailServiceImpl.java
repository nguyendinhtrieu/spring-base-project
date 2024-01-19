package com.tzyel.springbaseproject.service.impl;

import com.tzyel.springbaseproject.config.properties.SmtpProperties;
import com.tzyel.springbaseproject.service.MailService;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Properties;

@Slf4j
@Service
@ConditionalOnProperty(value = {"application.mail.serviceEnvironment"}, havingValue = "smtp")
public class SmtpMailServiceImpl implements MailService {
    @Value("${application.mail.enable}")
    private boolean isEnableMail;

    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";

    private final SmtpProperties smtpProperties;

    public SmtpMailServiceImpl(SmtpProperties smtpProperties) {
        this.smtpProperties = smtpProperties;
    }

    @Override
    public void sendMail(String subject, String content, List<String> sendToEmails, List<String> ccEmails, List<String> bccEmails) throws Exception {
        if (!isEnableMail) {
            log.info("Mail is not enabled. No mail will be sent.");
            return;
        }

        Message message = buildEmail(subject, sendToEmails, ccEmails, bccEmails);
        message.setContent(content, CONTENT_TYPE_TEXT_HTML);
        Transport.send(message);
    }

    private Message buildEmail(String subject, List<String> sendToEmails, List<String> ccEmails, List<String> bccEmails) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpProperties.getHost());
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", smtpProperties.getPort());
        props.put("mail.smtp.ssl.enable", smtpProperties.getSslEnable());

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpProperties.getUsername(), smtpProperties.getPassword());
                    }
                });

        Message message = new MimeMessage(session);

        addListRecipient(sendToEmails, message, Message.RecipientType.TO);
        addListRecipient(ccEmails, message, Message.RecipientType.CC);
        addListRecipient(bccEmails, message, Message.RecipientType.BCC);

        message.setFrom(new InternetAddress(smtpProperties.getSender()));
        message.setSubject(subject);

        return message;
    }

    private void addListRecipient(List<String> listEmails, Message message, Message.RecipientType recipientType) throws MessagingException {
        if (!CollectionUtils.isEmpty(listEmails)) {
            message.setRecipients(recipientType, InternetAddress.parse(String.join(",", listEmails)));
        }
    }
}
