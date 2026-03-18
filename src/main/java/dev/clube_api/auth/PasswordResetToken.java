package dev.clube_api.auth;

import dev.clube_api.usuario.model.UsuarioModel;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean used = false;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, UsuarioModel usuario, LocalDateTime expiryDate, boolean used) {
        this.token = token;
        this.usuario = usuario;
        this.expiryDate = expiryDate;
        this.used = used;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
