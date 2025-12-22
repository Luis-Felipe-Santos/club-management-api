package dev.clube_api.clube.repository;

import dev.clube_api.clube.model.ClubeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubeRepository extends JpaRepository<ClubeModel, Long> {

    boolean existsByCnpj(String cnpj);

}
