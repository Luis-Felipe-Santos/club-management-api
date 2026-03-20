package dev.clube_api.auth;

public interface EmailService {
    void sendPasswordResetEmail(String to, String resetLink, String userName);
}
