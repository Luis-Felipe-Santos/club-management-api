package dev.clube_api.socio.repository;

import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.socio.model.SocioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocioRepository extends JpaRepository<SocioModel, Long> {
    List<SocioModel> findByClube(ClubeModel clube);

}
