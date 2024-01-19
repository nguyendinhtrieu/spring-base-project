package com.tzyel.springbaseproject.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailAttachmentDto {
    private String fileName;
    private byte[] fileContent;
    private String contentType;
}
