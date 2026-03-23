package dev.clube_api.plano.dto;


import dev.clube_api.plano.enums.PeriodicidadePlano;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoCreateDTO {
    @NotBlank
    private String nome;

    @NotNull
    private BigDecimal valor;

    @NotNull
    private PeriodicidadePlano periodicidade;

    private Long clubeId;
}
