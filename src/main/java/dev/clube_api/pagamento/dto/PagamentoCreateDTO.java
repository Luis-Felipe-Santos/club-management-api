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
public class PagamentoCreateDTO {
    private Long socioPlanoId;

    private YearMonth competencia;

    private BigDecimal desconto;
    private BigDecimal acrescimo;

    private LocalDate dataVencimento;

    private String observacao;
}
