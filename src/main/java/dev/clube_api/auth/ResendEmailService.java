package dev.clube_api.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ResendEmailService implements EmailService {

    @Value("${RESEND_API_KEY}")
    private String apiKey;

    @Value("${EMAIL_FROM}")
    private String from;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendPasswordResetEmail(String to, String resetLink, String userName) {

        String url = "https://api.resend.com/emails";

        String html = buildResetPasswordHtml(resetLink, userName);

        Map<String, Object> body = Map.of(
                "from", from,
                "to", List.of(to),
                "subject", "Recuperação de senha",
                "html", html
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Erro ao enviar email: " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar email com Resend", e);
        }
    }

    private String buildResetPasswordHtml(String resetLink, String userName) {
        return """
            <div style="font-family: Arial, sans-serif; background-color: #f6f9fc; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background: #ffffff; padding: 30px; border-radius: 8px;">
                    
                    <h2 style="color: #111827;">Recuperação de senha</h2>

                    <p style="color: #374151;">Olá, %s</p>

                    <p style="color: #374151;">
                        Recebemos uma solicitação para redefinir sua senha.
                    </p>

                    <p style="color: #374151;">
                        Clique no botão abaixo para criar uma nova senha:
                    </p>

                    <div style="margin: 24px 0; text-align: center;">
                        <a href="%s"
                           style="background-color: #16a34a; color: #ffffff; padding: 12px 20px;
                                  text-decoration: none; border-radius: 6px; font-weight: bold;">
                            Redefinir senha
                        </a>
                    </div>

                    <p style="color: #6b7280; font-size: 13px;">
                        Este link expira em 30 minutos.
                    </p>

                    <p style="color: #6b7280; font-size: 13px;">
                        Se você não solicitou essa alteração, ignore este email.
                    </p>

                    <hr style="margin: 24px 0; border: none; border-top: 1px solid #e5e7eb;" />

                    <p style="color: #9ca3af; font-size: 12px;">
                        © Clubly
                    </p>
                </div>
            </div>
            """.formatted(userName, resetLink);
    }
}