package me.cyberproton.ocean.features.email;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@AllArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendEmailUsingTemplate(EmailTemplateRequest request) {
        Context context = new Context();
        if (request.getModel() != null) {
            context.setVariables(request.getModel());
        }
        String htmlBody = templateEngine.process(request.getTemplate(), context);
        sendHtmlEmail(
                EmailRequest.builder()
                        .to(request.getTo())
                        .subject(request.getSubject())
                        .content(htmlBody)
                        .build());
    }

    public void sendHtmlEmail(EmailRequest request) {
        mailSender.send(
                mimeMessage -> {
                    mimeMessage.setRecipient(
                            Message.RecipientType.TO, new InternetAddress(request.getTo()));
                    mimeMessage.setSubject(request.getSubject());
                    mimeMessage.setContent(request.getContent(), "text/html");
                });
    }
}
