package dev.clube_api.plano.dto;

import dev.clube_api.plano.enums.PeriodicidadePlano;
import dev.clube_api.plano.enums.StatusPlano;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoUpdateDTO {
    private String nome;
    private BigDecimal valor;
    private PeriodicidadePlano periodicidade;
}
