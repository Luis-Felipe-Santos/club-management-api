package dev.clube_api.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    public SmtpEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Recuperação de senha");
        message.setText(
                "Olá,\n\n" +
                        "Recebemos uma solicitação para redefinir sua senha.\n\n" +
                        "Clique no link abaixo para criar uma nova senha:\n\n" +
                        resetLink + "\n\n" +
                        "Se você não solicitou essa alteração, ignore este email."
        );

        mailSender.send(message);
    }
}
