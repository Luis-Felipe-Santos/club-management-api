package dev.clube_api.socio_plano_historico.mapper;


import dev.clube_api.socio_plano_historico.dto.SocioPlanoHistoricoResponseDTO;
import dev.clube_api.socio_plano_historico.model.SocioPlanoHistoricoModel;
import org.springframework.stereotype.Component;

@Component
public class SocioPlanoHistoricoMapper {
    public SocioPlanoHistoricoResponseDTO toDTO(SocioPlanoHistoricoModel model) {

        SocioPlanoHistoricoResponseDTO dto = new SocioPlanoHistoricoResponseDTO();

        dto.setAcao(model.getAcao());
        dto.setStatusAnterior(model.getStatusAnterior());
        dto.setStatusNovo(model.getStatusNovo());
        dto.setUsuarioNome(model.getUsuario().getNome());
        dto.setData(model.getCreatedAt());

        return dto;
    }
}
