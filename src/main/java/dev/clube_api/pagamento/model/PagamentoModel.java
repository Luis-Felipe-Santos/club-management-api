package dev.clube_api.pagamento.model;


import dev.clube_api.pagamento.enums.StatusPagamento;
import dev.clube_api.pagamento.service.PagamentoService;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(
        name = "pagamentos",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"socio_plano_id", "competencia"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "socio_plano_id", nullable = false)
    private SocioPlanoModel socioPlano;

    @Column(nullable = false)
    private YearMonth competencia;

    @Column(nullable = false)
    private BigDecimal valorBase;

    @Column(nullable = false)
    private BigDecimal valorFinal;

    private BigDecimal desconto;
    private BigDecimal acrescimo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status;

    private LocalDate dataVencimento;
    private LocalDate dataPagamento;

    private String observacao;

}
