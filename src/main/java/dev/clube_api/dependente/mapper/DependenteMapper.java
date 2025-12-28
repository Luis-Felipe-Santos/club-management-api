package dev.clube_api.dependente.mapper;

import dev.clube_api.dependente.dto.DependenteCreateDTO;
import dev.clube_api.dependente.dto.DependenteResponseDTO;
import dev.clube_api.dependente.dto.DependenteResumoDTO;
import dev.clube_api.dependente.dto.DependenteUpdateDTO;
import dev.clube_api.dependente.enums.StatusDependente;
import dev.clube_api.dependente.model.DependenteModel;
import dev.clube_api.socio.model.SocioModel;
import org.springframework.stereotype.Component;

@Component
public class DependenteMapper {

    public DependenteModel toEntity(DependenteCreateDTO dto, SocioModel socio){
        DependenteModel dependente = new DependenteModel();

        dependente.setImagemUrl(dto.getImagemUrl());
        dependente.setNome(dto.getNome());
        dependente.setParentesco(dto.getParentesco());

        dependente.setSocio(socio);
        dependente.setStatus(StatusDependente.ATIVO);

        return dependente;
    }

    public DependenteResponseDTO toResponseDTO(DependenteModel dependente) {
        return new DependenteResponseDTO(
                dependente.getId(),
                dependente.getImagemUrl(),
                dependente.getNome(),
                dependente.getParentesco(),
                dependente.getStatus(),
                dependente.getSocio().getId(),
                dependente.getSocio().getNome(),
                dependente.getCreatedAt(),
                dependente.getUpdatedAt()
        );
    }

    public DependenteResumoDTO toResumoDTO(DependenteModel dependente) {
        return new DependenteResumoDTO(
                dependente.getId(),
                dependente.getNome(),
                dependente.getParentesco(),
                dependente.getStatus()
        );
    }

    public void updateEntity(DependenteModel dependente, DependenteUpdateDTO dto) {

        if (dto.getImagemUrl() != null) {
            dependente.setImagemUrl(dto.getImagemUrl());
        }

        if (dto.getNome() != null) {
            dependente.setNome(dto.getNome());
        }

        if (dto.getParentesco() != null) {
            dependente.setParentesco(dto.getParentesco());
        }
    }


}
