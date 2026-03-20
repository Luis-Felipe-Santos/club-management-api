package dev.clube_api.auth;

import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public PasswordResetService(
            PasswordResetTokenRepository passwordResetTokenRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void forgotPassword(String email) {
        if (email == null || email.isBlank()) {
            return;
        }

        passwordResetTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return;
        }

        UsuarioModel usuario = usuarioOpt.get();

        passwordResetTokenRepository.deleteByUsuario(usuario);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUsuario(usuario);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);

        passwordResetTokenRepository.save(resetToken);

        String resetLink = frontendUrl + "/auth/resetPassword?token=" + token;

        String nome = usuario.getNome() != null && !usuario.getNome().isBlank()
                ? usuario.getNome()
                : "usuário";

        emailService.sendPasswordResetEmail(usuario.getEmail(), resetLink, nome);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDTO request) {
        if (request.token() == null || request.token().isBlank()) {
            throw new IllegalArgumentException("Token é obrigatório");
        }

        if (request.newPassword() == null || request.newPassword().isBlank()) {
            throw new IllegalArgumentException("Nova senha é obrigatória");
        }

        if (request.confirmPassword() == null || request.confirmPassword().isBlank()) {
            throw new IllegalArgumentException("Confirmação de senha é obrigatória");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("As senhas não coincidem");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (resetToken.isUsed()) {
            throw new IllegalArgumentException("Token já foi utilizado");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expirado");
        }

        UsuarioModel usuario = resetToken.getUsuario();

        usuario.setSenha(passwordEncoder.encode(request.newPassword()));
        usuarioRepository.save(usuario);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }
}