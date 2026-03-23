package dev.clube_api.clube.repository;

import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubeRepository extends JpaRepository<ClubeModel, Long> {
    List<ClubeModel> findByAdmin(UsuarioModel admin);
    boolean existsByCnpj(String cnpj);


}
