package dev.clube_api.auth;

import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUsuario(UsuarioModel usuario);
    void deleteByExpiryDateBefore(LocalDateTime now);
}
