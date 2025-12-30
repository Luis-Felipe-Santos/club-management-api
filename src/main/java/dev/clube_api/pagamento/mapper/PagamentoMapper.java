package dev.clube_api.pagamento.mapper;

import dev.clube_api.pagamento.dto.InadimplenteDTO;
import dev.clube_api.pagamento.dto.PagamentoResponseDTO;
import dev.clube_api.pagamento.dto.PagamentoResumoDTO;
import dev.clube_api.pagamento.model.PagamentoModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class PagamentoMapper {

    public PagamentoResponseDTO toResponseDTO(PagamentoModel pagamento) {
        PagamentoResponseDTO dto = new PagamentoResponseDTO();

        dto.setCompetencia(pagamento.getCompetencia());

        dto.setValorBase(pagamento.getValorBase());
        dto.setValorFinal(pagamento.getValorFinal());
        dto.setDesconto(pagamento.getDesconto());
        dto.setAcrescimo(pagamento.getAcrescimo());

        dto.setStatus(pagamento.getStatus());
        dto.setDataVencimento(pagamento.getDataVencimento());
        dto.setDataPagamento(pagamento.getDataPagamento());
        dto.setObservacao(pagamento.getObservacao());

        return dto;
    }

    public PagamentoResumoDTO toResumoDTO(PagamentoModel pagamento) {
        PagamentoResumoDTO dto = new PagamentoResumoDTO();

        dto.setCompetencia(pagamento.getCompetencia());
        dto.setValorFinal(pagamento.getValorFinal());
        dto.setStatus(pagamento.getStatus());
        dto.setDataPagamento(pagamento.getDataPagamento());

        return dto;
    }
    public InadimplenteDTO toInadimplente(PagamentoModel pagamento) {

        int diasEmAtraso =
                (int) ChronoUnit.DAYS.between(
                        pagamento.getDataVencimento(),
                        LocalDate.now()
                );

        return new InadimplenteDTO(
                pagamento.getSocioPlano().getSocio().getId(),
                pagamento.getSocioPlano().getSocio().getNome(),
                pagamento.getSocioPlano().getPlano().getNome(),
                pagamento.getCompetencia(),
                pagamento.getValorFinal(),
                pagamento.getDataVencimento(),
                diasEmAtraso
        );
    }
}