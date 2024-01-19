package com.tzyel.springbaseproject.service;

import com.tzyel.springbaseproject.dto.mail.MailAttachmentDto;
import com.tzyel.springbaseproject.dto.mail.template.MailTemplateDto;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface MailService {
    @Async
    void sendMail(String subject, String content, List<MailAttachmentDto> attachmentList, List<String> toEmails, List<String> ccEmails, List<String> bccEmails) throws Exception;

    @Async
    default void sendMail(String subject, String content, List<String> toEmails, List<String> ccEmails, List<String> bccEmails) throws Exception {
        sendMail(subject, content, null, toEmails, ccEmails, bccEmails);
    }

    @Async
    default void sendMail(String subject, String content, List<String> sendToEmails) throws Exception {
        sendMail(subject, content, null, sendToEmails, null, null);
    }

    @Async
    default void sendMail(MailTemplateDto mailTemplateDto, List<String> sendToEmails, List<String> ccEmails, List<String> bccEmails) throws Exception {
        sendMail(mailTemplateDto.getSubject(), mailTemplateDto.getContentHtml(), mailTemplateDto.getAttachmentList(), sendToEmails, ccEmails, bccEmails);
    }

    @Async
    default void sendMail(MailTemplateDto mailTemplateDto, List<String> sendToEmails) throws Exception {
        sendMail(mailTemplateDto.getSubject(), mailTemplateDto.getContentHtml(), mailTemplateDto.getAttachmentList(), sendToEmails, null, null);
    }
}
