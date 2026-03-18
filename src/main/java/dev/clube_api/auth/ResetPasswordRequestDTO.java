package dev.clube_api.auth;

public record ResetPasswordRequestDTO(
        String token,
        String newPassword,
        String confirmPassword
) {
}