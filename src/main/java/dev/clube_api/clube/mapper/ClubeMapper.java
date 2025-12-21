package dev.clube_api.clube.mapper;

import dev.clube_api.clube.dto.ClubeCreateDTO;
import dev.clube_api.clube.dto.ClubeResponseDTO;
import dev.clube_api.clube.dto.ClubeUpdateDTO;
import dev.clube_api.clube.enums.StatusClube;
import dev.clube_api.clube.model.ClubeModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClubeMapper {

    // DTO de criação → Entity
    public ClubeModel toEntity(ClubeCreateDTO dto){
        ClubeModel clube = new ClubeModel();
        clube.setNome(dto.getNome());
        clube.setCnpj(dto.getCnpj());

        clube.setStatus(StatusClube.Ativo);
        clube.setDataCadastro(LocalDateTime.now());

        return clube;
    }

    // Entity → DTO de resposta
    public ClubeResponseDTO toResponseDTO(ClubeModel clube){
        ClubeResponseDTO dto = new ClubeResponseDTO();
        dto.setId(clube.getId());
        dto.setNome(clube.getNome());
        dto.setCnpj(clube.getCnpj());
        dto.setStatus(clube.getStatus());
        dto.setDataCadastro(clube.getDataCadastro());
        return dto;

    }

    public void updateEntity(ClubeModel clube, ClubeUpdateDTO dto) {
        if (dto.getNome() != null) {
            clube.setNome(dto.getNome());
        }
    }


}
