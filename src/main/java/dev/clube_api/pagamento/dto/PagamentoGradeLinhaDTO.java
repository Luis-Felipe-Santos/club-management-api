package dev.clube_api.pagamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoGradeLinhaDTO {
    private Long socioId;
    private String socioNome;
    private String socioImagemUrl;

    private Long socioPlanoId;
    private Long planoId;
    private String planoNome;

    private BigDecimal valorBase;

    private List<PagamentoMesDTO> meses;
}