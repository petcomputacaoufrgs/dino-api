package br.ufrgs.inf.pet.dinoapi.service.email;

import br.ufrgs.inf.pet.dinoapi.configuration.properties.EmailConfig;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {
    private final EmailConfig emailConfig;

    @Autowired
    public EmailServiceImpl(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    @Async("defaultThreadPoolTaskExecutor")
    public void sendEmail(String dest, String subject, String text) {
        try {
            final HtmlEmail email = new HtmlEmail();
            email.setStartTLSEnabled(true);
            email.setHostName(emailConfig.getHost());
            email.setSmtpPort(emailConfig.getPort());
            email.setAuthenticator(new DefaultAuthenticator(emailConfig.getUsername(), emailConfig.getPassword()));
            email.setFrom(emailConfig.getUsername(), "From");
            email.addTo(dest, "To");
            email.setSubject(subject);
            email.setHtmlMsg(text);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
