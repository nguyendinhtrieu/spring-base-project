package com.tzyel.springbaseproject.dto.mail.template;

import com.tzyel.springbaseproject.utils.MailTemplateUtils;

import java.util.Map;

public abstract class MailTemplateDto {
    public abstract String getTemplateFile();

    public abstract String getSubject();

    public abstract Map<String, Object> getTemplateModel();

    public String getContentHtml() {
        return MailTemplateUtils.getContentHtml(this);
    }
}
