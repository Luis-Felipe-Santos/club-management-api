package dev.clube_api.socio_plano.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioPlanoOptionDTO {
    private Long id;
    private Long socioId;
    private String socioNome;
    private Long planoId;
    private String planoNome;
}