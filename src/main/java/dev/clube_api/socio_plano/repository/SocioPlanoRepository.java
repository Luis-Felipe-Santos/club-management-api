package dev.clube_api.socio_plano.repository;

import dev.clube_api.plano.model.PlanoModel;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocioPlanoRepository extends JpaRepository<SocioPlanoModel, Long> {
    List<SocioPlanoModel> findBySocio(SocioModel socio);

    boolean existsBySocioAndPlanoAndStatus(SocioModel socio, PlanoModel plano, StatusSocioPlano status);
}
