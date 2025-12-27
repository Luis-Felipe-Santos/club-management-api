package dev.clube_api.socio_plano.dto;

import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioPlanoResumoDTO {
    private Long socioPlanoId;
    private Long id;
    private String nomePlano;
    private StatusSocioPlano status;
}
