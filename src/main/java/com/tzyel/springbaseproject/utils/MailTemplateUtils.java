package com.tzyel.springbaseproject.utils;

import com.tzyel.springbaseproject.dto.mail.template.MailTemplateDto;
import org.apache.commons.codec.CharEncoding;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * Utility class for processing email templates using Thymeleaf.
 */
public class MailTemplateUtils {
    private static final String PREFIX_MAIL_TEMPLATE = "mail-templates/";
    private static final String SUFFIX_MAIL_TEMPLATE = ".html";

    private static SpringTemplateEngine thymeleafTemplateEngine;

    /**
     * Retrieves or initializes the Thymeleaf template engine.
     *
     * @return The Thymeleaf template engine.
     */
    private static SpringTemplateEngine getThymeleafTemplateEngine() {
        if (thymeleafTemplateEngine == null) {
            thymeleafTemplateEngine = initThymeleafTemplateEngine();
        }

        return thymeleafTemplateEngine;
    }

    /**
     * Processes the Thymeleaf template and retrieves the HTML content.
     *
     * @param mailTemplateDto The data object containing template information and variables.
     * @return The processed HTML content.
     */
    public static String getContentHtml(MailTemplateDto mailTemplateDto) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(mailTemplateDto.getTemplateModel());
        return getThymeleafTemplateEngine().process(mailTemplateDto.getTemplateFile(), thymeleafContext);
    }

    /**
     * Configures the Thymeleaf template resolver for HTML templates.
     *
     * @return The configured Thymeleaf template resolver.
     */
    private static ITemplateResolver thymeleafTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(PREFIX_MAIL_TEMPLATE);
        templateResolver.setSuffix(SUFFIX_MAIL_TEMPLATE);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(CharEncoding.UTF_8);
        return templateResolver;
    }

    /**
     * Initializes and configures the Thymeleaf template engine.
     *
     * @return The initialized Thymeleaf template engine.
     */
    private static SpringTemplateEngine initThymeleafTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
        return templateEngine;
    }
}
