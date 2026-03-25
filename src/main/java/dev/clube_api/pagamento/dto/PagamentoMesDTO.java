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
public class PagamentoMesDTO {
    private Long pagamentoId;
    private YearMonth competencia;
    private BigDecimal valorFinal;
    private StatusPagamento status;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
}