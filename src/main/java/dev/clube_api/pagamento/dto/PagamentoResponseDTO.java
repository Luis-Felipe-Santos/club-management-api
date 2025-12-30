package dev.clube_api.pagamento.dto;


import dev.clube_api.pagamento.enums.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoResponseDTO {
    private YearMonth competencia;

    private BigDecimal valorBase;
    private BigDecimal valorFinal;

    private BigDecimal desconto;
    private BigDecimal acrescimo;

    private StatusPagamento status;

    private LocalDate dataVencimento;
    private LocalDate dataPagamento;

    private String observacao;
}
