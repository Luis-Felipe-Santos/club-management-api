package dev.clube_api.socio_plano.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioPlanoCreateDTO {

    @NotNull
    private Long socioId;

    @NotNull
    private Long planoId;
}
