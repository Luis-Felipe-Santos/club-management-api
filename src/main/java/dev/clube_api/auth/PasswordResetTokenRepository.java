package dev.clube_api.auth;

import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByExpiryDateBefore(LocalDateTime now);

    void deleteByUsuario(UsuarioModel usuario);
}