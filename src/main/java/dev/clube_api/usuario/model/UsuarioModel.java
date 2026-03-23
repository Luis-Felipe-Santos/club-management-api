package dev.clube_api.usuario.model;


import dev.clube_api.usuario.enums.RoleUsuario;
import dev.clube_api.usuario.enums.StatusUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imagem_url")
    private String imagemUrl;

    @Column(name="nome", nullable = false)
    private String nome;

    @Column(name="cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="senha", nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "permissao", nullable = false)
    private RoleUsuario role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusUsuario status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != StatusUsuario.BLOQUEADO;
    }

    @Override
    public boolean isEnabled() {
        return status == StatusUsuario.ATIVO;
    }
}
