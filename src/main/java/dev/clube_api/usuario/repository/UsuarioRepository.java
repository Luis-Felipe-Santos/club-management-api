package dev.clube_api.usuario.repository;

import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Optional<UsuarioModel> findByEmail(String email);
}
