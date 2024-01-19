package com.tzyel.springbaseproject.dto.mail.template;

import com.tzyel.springbaseproject.dto.mail.MailAttachmentDto;
import com.tzyel.springbaseproject.utils.MailTemplateUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class MailTemplateDto {
    protected List<MailAttachmentDto> attachmentList;

    public abstract String getTemplateFile();

    public abstract String getSubject();

    public abstract Map<String, Object> getTemplateModel();

    public String getContentHtml() {
        return MailTemplateUtils.getContentHtml(this);
    }
}
