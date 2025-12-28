package dev.clube_api.dependente.repository;

import dev.clube_api.dependente.enums.StatusDependente;
import dev.clube_api.dependente.model.DependenteModel;
import dev.clube_api.socio.model.SocioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DependenteRepository extends JpaRepository<DependenteModel, Long>{

    List<DependenteModel> findBySocio(SocioModel socio);

    List<DependenteModel> findBySocioAndStatus(SocioModel socio, StatusDependente status);
}
