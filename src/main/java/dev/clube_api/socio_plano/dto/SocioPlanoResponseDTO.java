package dev.clube_api.socio_plano.dto;


import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioPlanoResponseDTO {
    private Long id;

    private Long socioId;
    private String nomeSocio;

    private Long planoId;
    private String nomePlano;

    private StatusSocioPlano status;

    private LocalDateTime createdAt;

}
