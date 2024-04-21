package com.boilerplate.utils.email;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import freemarker.template.Configuration;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import com.boilerplate.utils.UtilsService;
import com.boilerplate.configuration.ExternalConfigs;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Map;


@Service
public class EmailService implements IEmailService {
    private static final Logger logger = LogManager.getLogger(EmailService.class);

    @Autowired
    public JavaMailSender emailSender;
    @Autowired
    Configuration fmConfiguration;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private ExternalConfigs externalConfigs;

    @Override
    @Async
    public void sendTestEmail(
            String email,
            Map<String, String> model
    ) {
        try {
            sendEmail(email, "Test Email Subject", model, "test.html");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private String geContentFromTemplate(Map<String, String> model, String template) {
        StringBuilder content = new StringBuilder();
        try {
            ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(), "/templates/");
            FileTemplateLoader ftl1 = new FileTemplateLoader(new File(utilsService.getTempDir("templates")));
            TemplateLoader[] loaders = new TemplateLoader[]{ctl, ftl1};
            MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
            fmConfiguration.setTemplateLoader(mtl);
            content.append(
                    FreeMarkerTemplateUtils.processTemplateIntoString(
                            fmConfiguration.getTemplate(template),
                            model
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private void sendEmail(String email, String subject, Map<String, String> model, String template) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mail = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setFrom(externalConfigs.getEmailSender(), externalConfigs.getEmailUsername());
        helper.setTo(email);
        helper.setSubject(subject);
        String body = geContentFromTemplate(model, template);
        helper.setText("", body);
        emailSender.send(mail);
    }

    private String formatExpirationValue(Integer expiration) {
        if (expiration < 60) {
            return expiration + " minutes";
        }
        if (expiration % 60 == 0) {
            return (expiration / 60) + " hours";
        }
        DecimalFormat formatter = new DecimalFormat("#0.0");
        double d = expiration / (double) 60;
        return formatter.format(d) + " hours";
    }
}
