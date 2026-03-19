package dev.clube_api.auth;

import org.springframework.beans.factory.annotation.Value;
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
    public void sendPasswordResetEmail(String to, String resetLink) {

        String url = "https://api.resend.com/emails";

        Map<String, Object> body = Map.of(
                "from", from,
                "to", List.of(to),
                "subject", "Recuperação de senha",
                "html", "<p>Clique no link para redefinir sua senha:</p>" +
                        "<a href=\"" + resetLink + "\">Redefinir senha</a>"
        );

        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        var request = new org.springframework.http.HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, request, String.class);
    }
}