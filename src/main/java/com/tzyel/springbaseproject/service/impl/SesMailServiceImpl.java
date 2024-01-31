package com.tzyel.springbaseproject.service.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.tzyel.springbaseproject.dto.mail.MailAttachmentDto;
import com.tzyel.springbaseproject.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(value = {"application.mail.serviceEnvironment"}, havingValue = "ses")
public class SesMailServiceImpl implements MailService {
    @Value("${application.mail.enable}")
    private boolean isEnableMail;

    @Value("${application.mail.ses.region}")
    private String region;

    @Value("${application.mail.ses.sender}")
    private String sender;

    @Override
    public void sendMail(String subject, String content, List<MailAttachmentDto> attachmentList, List<String> toEmails, List<String> ccEmails, List<String> bccEmails) throws Exception {
        if (!isEnableMail) {
            log.info("Mail is not enabled. No mail will be sent.");
            return;
        }
        sendAwsSesMail(subject, content, attachmentList, true, toEmails, ccEmails, bccEmails);
    }

    private void sendAwsSesMail(String subject, String content, List<MailAttachmentDto> attachmentList, boolean isHtmlContentType,
                                List<String> sendToEmails, List<String> ccEmails, List<String> bccEmails) throws Exception {
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(region).build();

        SendEmailRequest request = new SendEmailRequest();
        Destination destination = new Destination();
        destination.setToAddresses(sendToEmails);
        destination.setCcAddresses(ccEmails);
        destination.setBccAddresses(bccEmails);

        Message message = new Message();
        Body messageBody = new Body();
        if (isHtmlContentType) {
            messageBody.setHtml(new Content(content));
        } else {
            messageBody.setText(new Content(content));
        }

        message.setSubject(new Content(subject));
        message.setBody(messageBody);
        request.setSource(sender);
        request.setDestination(destination);
        request.setMessage(message);

        // TODO: Handle attachmentList
        log.debug("Avoid unused method parameters such as 'attachmentList' - {}.", attachmentList);

        client.sendEmail(request);
    }
}
