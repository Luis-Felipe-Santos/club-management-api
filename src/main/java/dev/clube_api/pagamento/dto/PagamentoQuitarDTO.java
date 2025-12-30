package dev.clube_api.pagamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoQuitarDTO {
    private LocalDate dataPagamento;
    private String observacao;
}
