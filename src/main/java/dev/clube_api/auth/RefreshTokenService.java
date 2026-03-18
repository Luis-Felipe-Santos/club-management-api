package dev.clube_api.auth;

import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken create(UsuarioModel usuario) {

        RefreshToken token = new RefreshToken();
        token.setUsuario(usuario);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusDays(7));

        return repository.save(token);
    }

    public RefreshToken validate(String token) {
        repository.deleteByExpiryDateBefore(LocalDateTime.now());

        token = token.trim();

        RefreshToken rt = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Sessão expirada. Faça login novamente."));

        if (rt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expirado");
        }

        return rt;
    }
    public void delete(RefreshToken token) {
        repository.delete(token);
    }
}