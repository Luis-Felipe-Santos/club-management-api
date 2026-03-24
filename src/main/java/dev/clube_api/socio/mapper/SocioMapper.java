package dev.clube_api.socio.mapper;

import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.socio.dto.SocioCreateDTO;
import dev.clube_api.socio.dto.SocioResponseDTO;
import dev.clube_api.socio.dto.SocioResumoDTO;
import dev.clube_api.socio.dto.SocioUpdateDTO;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio_plano.dto.SocioPlanoResumoDTO;
import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SocioMapper {

    public SocioModel toEntity(SocioCreateDTO dto, ClubeModel clube) {
        SocioModel socio = new SocioModel();

        socio.setNome(dto.getNome());
        socio.setTipoDocumento(dto.getTipoDocumento());
        socio.setDocumento(dto.getDocumento());
        socio.setTelefone(dto.getTelefone());
        socio.setEmail(dto.getEmail());
        socio.setEndereco(dto.getEndereco());
        socio.setImagemUrl(dto.getImagemUrl());
        socio.setClube(clube);

        return socio;
    }

    public SocioResponseDTO toResponseDTO(SocioModel socio, List<SocioPlanoResumoDTO> planos) {
        SocioResponseDTO dto = new SocioResponseDTO();

        dto.setId(socio.getId());
        dto.setNome(socio.getNome());
        dto.setTipoDocumento(socio.getTipoDocumento());
        dto.setDocumento(socio.getDocumento());
        dto.setTelefone(socio.getTelefone());
        dto.setEmail(socio.getEmail());
        dto.setEndereco(socio.getEndereco());
        dto.setImagemUrl(socio.getImagemUrl());
        dto.setStatus(socio.getStatus());
        dto.setPlanos(planos);
        dto.setCreatedAt(socio.getCreatedAt());
        dto.setUpdatedAt(socio.getUpdatedAt());

        return dto;
    }

    public SocioResumoDTO toResumoDTO(
            SocioModel socio,
            Long socioPlanoId,
            Long planoId,
            String planoNome,
            StatusSocioPlano statusPlano,
            Boolean possuiPlanoAtivo
    ) {
        SocioResumoDTO dto = new SocioResumoDTO();

        dto.setId(socio.getId());
        dto.setNome(socio.getNome());
        dto.setDocumento(socio.getDocumento());
        dto.setTelefone(socio.getTelefone());
        dto.setEmail(socio.getEmail());
        dto.setImagemUrl(socio.getImagemUrl());
        dto.setEndereco(socio.getEndereco());
        dto.setStatus(socio.getStatus());

        dto.setSocioPlanoId(socioPlanoId);
        dto.setPlanoId(planoId);
        dto.setPlanoNome(planoNome);
        dto.setStatusPlano(statusPlano);
        dto.setPossuiPlanoAtivo(possuiPlanoAtivo);

        return dto;
    }

    public void updateEntity(SocioModel socio, SocioUpdateDTO dto) {
        if (dto.getNome() != null) {
            socio.setNome(dto.getNome());
        }

        if (dto.getTipoDocumento() != null) {
            socio.setTipoDocumento(dto.getTipoDocumento());
        }

        if (dto.getDocumento() != null) {
            socio.setDocumento(dto.getDocumento());
        }

        if (dto.getTelefone() != null) {
            socio.setTelefone(dto.getTelefone());
        }

        if (dto.getEmail() != null) {
            socio.setEmail(dto.getEmail());
        }

        if (dto.getEndereco() != null) {
            socio.setEndereco(dto.getEndereco());
        }

        if (dto.getImagemUrl() != null) {
            socio.setImagemUrl(dto.getImagemUrl());
        }
    }
}