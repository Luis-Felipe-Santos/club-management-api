package dev.clube_api.plano.repository;

import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.plano.enums.StatusPlano;
import dev.clube_api.plano.model.PlanoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanoRepository extends JpaRepository<PlanoModel, Long> {
    boolean existsByNomeAndClube(String nome, ClubeModel clube);

    List<PlanoModel> findByClube(ClubeModel clube);

    List<PlanoModel> findByClubeAndStatus(
            ClubeModel clube,
            StatusPlano status
    );
}
