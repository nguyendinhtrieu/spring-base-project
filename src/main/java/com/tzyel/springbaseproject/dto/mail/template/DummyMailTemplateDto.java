package com.tzyel.springbaseproject.dto.mail.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class DummyMailTemplateDto extends MailTemplateDto {
    private static final String SUBJECT = "SUBJECT OF {0}";
    private static final String NAME_TEMPLATE_FILE = "dummy-mail.html";

    private String name;
    private String value;

    @Override
    public String getTemplateFile() {
        return NAME_TEMPLATE_FILE;
    }

    @Override
    public String getSubject() {
        return MessageFormat.format(SUBJECT, name);
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("name", name);
        templateModel.put("value", value);
        return templateModel;
    }
}
