package com.tzyel.springbaseproject.service;

import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface MailService {
    @Async
    void sendMail(String subject, String content, List<String> sendToEmails) throws Exception;

    @Async
    void sendMail(String subject, String content, List<String> sendToEmails, List<String> ccEmails, List<String> bccEmails) throws Exception;
}
