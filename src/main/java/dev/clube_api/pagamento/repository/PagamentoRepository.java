package dev.clube_api.pagamento.repository;

import dev.clube_api.pagamento.model.PagamentoModel;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.YearMonth;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<PagamentoModel, Long> {

    boolean existsBySocioPlanoAndCompetencia(SocioPlanoModel socioPlano, YearMonth competencia);


    List<PagamentoModel> findBySocioPlano_Socio_IdAndSocioPlano_Socio_Clube_Id(
            Long socioId,
            Long clubeId
    );


    @Query("""
        SELECT p
        FROM PagamentoModel p
        WHERE p.competencia = :competencia
          AND p.status = dev.clube_api.pagamento.enums.StatusPagamento.PENDENTE
          AND p.dataVencimento < CURRENT_DATE
          AND p.socioPlano.socio.clube.id = :clubeId
    """)
    List<PagamentoModel> buscarInadimplentes(
            YearMonth competencia,
            Long clubeId
    );

}
