package dev.clube_api.pagamento.service;

import dev.clube_api.pagamento.dto.*;
import dev.clube_api.pagamento.enums.StatusPagamento;
import dev.clube_api.pagamento.mapper.PagamentoMapper;
import dev.clube_api.pagamento.model.PagamentoModel;
import dev.clube_api.pagamento.repository.PagamentoRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio.repository.SocioRepository;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import dev.clube_api.socio_plano.repository.SocioPlanoRepository;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final SocioPlanoRepository socioPlanoRepository;
    private final PagamentoMapper pagamentoMapper;

    public PagamentoService(
            PagamentoRepository pagamentoRepository,
            SocioPlanoRepository socioPlanoRepository,
            PagamentoMapper pagamentoMapper
    ) {
        this.pagamentoRepository = pagamentoRepository;
        this.socioPlanoRepository = socioPlanoRepository;
        this.pagamentoMapper = pagamentoMapper;
    }

    // =========================
    // 🔹 CRIAR COBRANÇA
    // =========================
    public PagamentoResponseDTO criar(PagamentoCreateDTO dto, UsuarioModel usuarioLogado) {

        SocioPlanoModel socioPlano = socioPlanoRepository.findById(dto.getSocioPlanoId())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Vínculo sócio/plano não encontrado")
                );

        validarClube(socioPlano.getSocio(), usuarioLogado);

        // evita duplicidade por competência
        if (pagamentoRepository.existsBySocioPlanoAndCompetencia(
                socioPlano,
                dto.getCompetencia()
        )) {
            throw new IllegalStateException("Já existe pagamento para esta competência");
        }

        BigDecimal valorBase = socioPlano.getPlano().getValor();
        BigDecimal desconto = dto.getDesconto() != null ? dto.getDesconto() : BigDecimal.ZERO;
        BigDecimal acrescimo = dto.getAcrescimo() != null ? dto.getAcrescimo() : BigDecimal.ZERO;

        BigDecimal valorFinal = valorBase.subtract(desconto).add(acrescimo);

        PagamentoModel pagamento = new PagamentoModel();
        pagamento.setSocioPlano(socioPlano);
        pagamento.setCompetencia(dto.getCompetencia());
        pagamento.setValorBase(valorBase);
        pagamento.setDesconto(desconto);
        pagamento.setAcrescimo(acrescimo);
        pagamento.setValorFinal(valorFinal);
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setDataVencimento(dto.getDataVencimento());
        pagamento.setObservacao(dto.getObservacao());

        return pagamentoMapper.toResponseDTO(
                pagamentoRepository.save(pagamento)
        );
    }

    // =========================
    // 🔹 QUITAR PAGAMENTO
    // =========================
    public PagamentoResponseDTO quitar(Long pagamentoId, PagamentoQuitarDTO dto, UsuarioModel usuarioLogado) {

        PagamentoModel pagamento = buscarPagamentoDoClube(pagamentoId, usuarioLogado);

        if (pagamento.getStatus() == StatusPagamento.PAGO) {
            throw new IllegalStateException("Pagamento já foi quitado");
        }

        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setDataPagamento(
                dto.getDataPagamento() != null
                        ? dto.getDataPagamento()
                        : LocalDate.now()
        );

        return pagamentoMapper.toResponseDTO(
                pagamentoRepository.save(pagamento)
        );
    }

    // =========================
    // 🔹 AJUSTAR PAGAMENTO
    // =========================
    public PagamentoResponseDTO ajustar(
            Long pagamentoId,
            PagamentoAjusteDTO dto,
            UsuarioModel usuarioLogado
    ) {
        PagamentoModel pagamento =
                buscarPagamentoDoClube(pagamentoId, usuarioLogado);

        if (pagamento.getStatus() == StatusPagamento.PAGO) {
            throw new IllegalStateException(
                    "Não é possível ajustar um pagamento já quitado"
            );
        }

        // ajuste administrativo apenas
        if (dto.getDataPagamento() != null) {
            pagamento.setDataPagamento(dto.getDataPagamento());
        }

        if (dto.getObservacao() != null) {
            pagamento.setObservacao(dto.getObservacao());
        }

        return pagamentoMapper.toResponseDTO(
                pagamentoRepository.save(pagamento)
        );
    }

    // =========================
    // 🔹 LISTAR POR SÓCIO
    // =========================
    public List<PagamentoResumoDTO> listarPorSocio(
            Long socioId,
            UsuarioModel usuarioLogado
    ) {
        return pagamentoRepository
                .findBySocioPlano_Socio_IdAndSocioPlano_Socio_Clube_Id(
                        socioId,
                        usuarioLogado.getClube().getId()
                )
                .stream()
                .map(pagamentoMapper::toResumoDTO)
                .toList();
    }

    // =========================
    // 🔒 MÉTODOS AUXILIARES
    // =========================
    private PagamentoModel buscarPagamentoDoClube(Long pagamentoId, UsuarioModel usuarioLogado) {
        PagamentoModel pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Pagamento não encontrado")
                );

        validarClube(pagamento.getSocioPlano().getSocio(), usuarioLogado);
        return pagamento;
    }

    private void validarClube(SocioModel socio, UsuarioModel usuarioLogado) {
        if (!socio.getClube().getId().equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Registro não pertence ao seu clube");
        }
    }

    public List<InadimplenteDTO> gerarRelatorioInadimplentes(
            YearMonth competencia,
            UsuarioModel usuarioLogado
    ) {
        return pagamentoRepository
                .buscarInadimplentes(
                        competencia,
                        usuarioLogado.getClube().getId()
                )
                .stream()
                .map(pagamentoMapper::toInadimplente)
                .toList();
    }

}
