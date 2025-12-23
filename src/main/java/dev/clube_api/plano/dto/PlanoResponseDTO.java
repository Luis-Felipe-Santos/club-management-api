package dev.clube_api.plano.dto;


import dev.clube_api.plano.enums.PeriodicidadePlano;
import dev.clube_api.plano.enums.StatusPlano;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoResponseDTO {
    private Long id;
    private String nome;
    private BigDecimal valor;
    private PeriodicidadePlano periodicidade;
    private StatusPlano status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
