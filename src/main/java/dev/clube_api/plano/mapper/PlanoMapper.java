package dev.clube_api.plano.mapper;


import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.plano.dto.PlanoCreateDTO;
import dev.clube_api.plano.dto.PlanoResponseDTO;
import dev.clube_api.plano.dto.PlanoUpdateDTO;
import dev.clube_api.plano.enums.StatusPlano;
import dev.clube_api.plano.model.PlanoModel;
import org.springframework.stereotype.Component;

@Component
public class PlanoMapper {
    public PlanoModel toEntity(PlanoCreateDTO dto, ClubeModel clube){
        PlanoModel plano = new PlanoModel();

        plano.setNome(dto.getNome());
        plano.setValor(dto.getValor());
        plano.setPeriodicidade(dto.getPeriodicidade());
        plano.setStatus(StatusPlano.ATIVO);
        plano.setClube(clube);

        return plano;
    }

    public PlanoResponseDTO toResponseDTO(PlanoModel plano) {
        PlanoResponseDTO dto = new PlanoResponseDTO();

        dto.setId(plano.getId());
        dto.setNome(plano.getNome());
        dto.setValor(plano.getValor());
        dto.setPeriodicidade(plano.getPeriodicidade());
        dto.setStatus(plano.getStatus());

        dto.setCreatedAt(plano.getCreatedAt());
        dto.setUpdatedAt(plano.getUpdatedAt());
        return dto;
    }

    public void updateEntity(PlanoModel plano, PlanoUpdateDTO dto) {

        if (dto.getNome() != null) {
            plano.setNome(dto.getNome());
        }

        if (dto.getValor() != null) {
            plano.setValor(dto.getValor());
        }

        if (dto.getPeriodicidade() != null) {
            plano.setPeriodicidade(dto.getPeriodicidade());
        }

    }
}


