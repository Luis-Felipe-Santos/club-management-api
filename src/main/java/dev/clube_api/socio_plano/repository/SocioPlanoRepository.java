package dev.clube_api.socio_plano.repository;

import dev.clube_api.socio_plano.model.SocioPlanoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocioPlanoRepository extends JpaRepository<SocioPlanoModel, Long> {
}
