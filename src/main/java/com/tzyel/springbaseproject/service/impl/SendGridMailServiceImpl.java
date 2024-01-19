package com.tzyel.springbaseproject.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.tzyel.springbaseproject.config.properties.SendGridProperties;
import com.tzyel.springbaseproject.dto.mail.MailAttachmentDto;
import com.tzyel.springbaseproject.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Service
@ConditionalOnProperty(value = {"application.mail.serviceEnvironment"}, havingValue = "sendgrid")
public class SendGridMailServiceImpl implements MailService {
    private final SendGridProperties sendGridProperties;

    @Value("${application.mail.enable}")
    private boolean isEnableMail;

    public SendGridMailServiceImpl(SendGridProperties sendGridProperties) {
        this.sendGridProperties = sendGridProperties;
    }

    @Override
    public void sendMail(String subject, String content, List<MailAttachmentDto> attachmentList,
                         List<String> toEmails, List<String> ccEmails, List<String> bccEmails) throws Exception {
        if (!isEnableMail) {
            log.info("Mail is not enabled. No mail will be sent.");
            return;
        }

        Mail mail = buildMailWithContent(subject, content, toEmails, ccEmails, bccEmails);

        if (!CollectionUtils.isEmpty(attachmentList)) {
            attachmentList.forEach(mailAttachmentDto -> {
                Attachments attachments = new Attachments();
                attachments.setType(mailAttachmentDto.getContentType());
                attachments.setFilename(mailAttachmentDto.getFileName());
                attachments.setContent(Base64.getEncoder().encodeToString(mailAttachmentDto.getFileContent()));
                attachments.setDisposition("attachment");
                mail.addAttachments(attachments);
            });
        }

        handleSendMail(mail);
    }

    private void handleSendMail(Mail mail) throws Exception {
        SendGrid sg = new SendGrid(sendGridProperties.getApiKey());
        sg.addRequestHeader("X-Mock", "true");

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
        log.debug(String.valueOf(response.getStatusCode()));
        log.debug(response.getBody());
    }

    private Mail buildMailWithContent(String subject, String content, List<String> toEmails, List<String> ccEmails, List<String> bccEmails) {
        Mail mail = new Mail();

        mail.setFrom(new Email(sendGridProperties.getFromEmail(), sendGridProperties.getFromName()));
        mail.setSubject(subject);

        Personalization personalization = new Personalization();
        addEmailAddress(toEmails, personalization::addTo);
        addEmailAddress(ccEmails, personalization::addCc);
        addEmailAddress(bccEmails, personalization::addBcc);
        mail.addPersonalization(personalization);

        Content contentHtml = new Content();
        contentHtml.setType(ContentType.TEXT_HTML.getMimeType());
        contentHtml.setValue(content);
        mail.addContent(contentHtml);

        return mail;
    }

    private void addEmailAddress(List<String> emailList, Consumer<Email> addEmailConsumer) {
        if (!CollectionUtils.isEmpty(emailList)) {
            emailList.forEach(email -> {
                Email to = new Email();
                to.setEmail(email);
                addEmailConsumer.accept(to);
            });
        }
    }
}
