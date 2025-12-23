package dev.clube_api.socio_plano.mapper;


import dev.clube_api.plano.model.PlanoModel;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio_plano.dto.SocioPlanoResponseDTO;
import dev.clube_api.socio_plano.dto.SocioPlanoResumoDTO;
import dev.clube_api.socio_plano.dto.SocioPlanoUpdateDTO;
import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import org.springframework.stereotype.Component;

@Component
public class SocioPlanoMapper {

    public SocioPlanoModel toEntity(SocioModel socio, PlanoModel plano){
        SocioPlanoModel sp = new SocioPlanoModel();
        sp.setSocio(socio);
        sp.setPlano(plano);
        sp.setStatus(StatusSocioPlano.ATIVO);
        return sp;
    }

    public SocioPlanoResponseDTO toResponseDTO(SocioPlanoModel sp) {
        return new SocioPlanoResponseDTO(
                sp.getId(),
                sp.getSocio().getId(),
                sp.getSocio().getNome(),
                sp.getPlano().getId(),
                sp.getPlano().getNome(),
                sp.getStatus(),
                sp.getCreatedAt()
        );
    }
    public SocioPlanoResumoDTO toResumoDTO(SocioPlanoModel sp) {
        return new SocioPlanoResumoDTO(
                sp.getId(),
                sp.getPlano().getNome(),
                sp.getStatus()
        );
    }

    public void updateEntity(SocioPlanoModel socioPlano, SocioPlanoUpdateDTO dto) {
        socioPlano.setStatus(dto.getStatus());
    }
}
