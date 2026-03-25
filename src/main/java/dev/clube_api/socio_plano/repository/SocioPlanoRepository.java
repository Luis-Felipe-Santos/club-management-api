package dev.clube_api.socio_plano.repository;

import dev.clube_api.plano.model.PlanoModel;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocioPlanoRepository extends JpaRepository<SocioPlanoModel, Long> {
    List<SocioPlanoModel> findBySocio(SocioModel socio);

    boolean existsBySocioAndPlanoAndStatus(SocioModel socio, PlanoModel plano, StatusSocioPlano status);

    List<SocioPlanoModel> findByPlanoAndStatusAndSocio_Clube_Id(
            PlanoModel plano,
            StatusSocioPlano status,
            Long clubeId
    );

    List<SocioPlanoModel> findBySocio_Clube_IdAndStatus(Long clubeId, StatusSocioPlano status);

    List<SocioPlanoModel> findBySocio_Clube_IdAndPlano_IdAndStatus(
            Long clubeId,
            Long planoId,
            StatusSocioPlano status
    );

    Optional<SocioPlanoModel> findFirstBySocioAndStatusOrderByCreatedAtDesc(
            SocioModel socio,
            StatusSocioPlano status
    );
}