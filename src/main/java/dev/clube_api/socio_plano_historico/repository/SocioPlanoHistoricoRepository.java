package dev.clube_api.socio_plano_historico.repository;

import dev.clube_api.socio_plano.model.SocioPlanoModel;
import dev.clube_api.socio_plano_historico.model.SocioPlanoHistoricoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocioPlanoHistoricoRepository extends JpaRepository<SocioPlanoHistoricoModel, Long> {
    List<SocioPlanoHistoricoModel> findBySocioPlanoOrderByCreatedAtDesc(SocioPlanoModel socioPlano);
}
