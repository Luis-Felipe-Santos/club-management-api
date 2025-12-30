package dev.clube_api.pagamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InadimplenteDTO {

    // identificação
    private Long socioId;
    private String nomeSocio;

    // plano
    private String nomePlano;

    // pagamento
    private YearMonth competencia;
    private BigDecimal valorDevido;
    private LocalDate dataVencimento;

    // status do atraso
    private Integer diasEmAtraso;
}