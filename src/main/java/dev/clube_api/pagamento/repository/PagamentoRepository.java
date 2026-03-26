package dev.clube_api.pagamento.repository;

import dev.clube_api.pagamento.enums.StatusPagamento;
import dev.clube_api.pagamento.model.PagamentoModel;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<PagamentoModel, Long> {

    boolean existsBySocioPlanoAndCompetencia(SocioPlanoModel socioPlano, YearMonth competencia);


    List<PagamentoModel> findBySocioPlano_Socio_IdAndSocioPlano_Socio_Clube_Id(
            Long socioId,
            Long clubeId
    );

    List<PagamentoModel> findBySocioPlano_IdInAndCompetenciaBetween(
            List<Long> socioPlanoIds,
            YearMonth inicio,
            YearMonth fim
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

    @Query("""
        SELECT p
        FROM PagamentoModel p
        WHERE p.socioPlano.socio.clube.id = :clubeId
          AND (:planoId IS NULL OR p.socioPlano.plano.id = :planoId)
          AND (:competencia IS NULL OR p.competencia = :competencia)
          AND (:status IS NULL OR p.status = :status)
          AND (
                :busca IS NULL
                OR :busca = ''
                OR LOWER(p.socioPlano.socio.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
              )
        ORDER BY p.competencia DESC, p.socioPlano.socio.nome ASC
    """)
    List<PagamentoModel> buscarComFiltros(
            @Param("clubeId") Long clubeId,
            @Param("planoId") Long planoId,
            @Param("competencia") YearMonth competencia,
            @Param("status") StatusPagamento status,
            @Param("busca") String busca
    );

}
