package dev.clube_api.pagamento.dto;

import dev.clube_api.pagamento.enums.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoListaDTO {
    private Long id;

    private Long socioId;
    private String socioNome;
    private String socioImagemUrl;

    private Long socioPlanoId;
    private Long planoId;
    private String planoNome;

    private YearMonth competencia;

    private BigDecimal valorBase;
    private BigDecimal valorFinal;

    private StatusPagamento status;

    private LocalDate dataVencimento;
    private LocalDate dataPagamento;

    private String observacao;
}