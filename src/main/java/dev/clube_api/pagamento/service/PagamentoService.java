package dev.clube_api.pagamento.service;

import dev.clube_api.clube.repository.ClubeRepository;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final SocioPlanoRepository socioPlanoRepository;
    private final SocioRepository socioRepository;
    private final ClubeRepository clubeRepository;
    private final PagamentoMapper pagamentoMapper;

    public PagamentoService(
            PagamentoRepository pagamentoRepository,
            SocioPlanoRepository socioPlanoRepository,
            SocioRepository socioRepository,
            ClubeRepository clubeRepository,
            PagamentoMapper pagamentoMapper
    ) {
        this.pagamentoRepository = pagamentoRepository;
        this.socioPlanoRepository = socioPlanoRepository;
        this.socioRepository = socioRepository;
        this.clubeRepository = clubeRepository;
        this.pagamentoMapper = pagamentoMapper;
    }

    public PagamentoResponseDTO criar(PagamentoCreateDTO dto, UsuarioModel usuarioLogado) {
        SocioPlanoModel socioPlano = socioPlanoRepository.findById(dto.getSocioPlanoId())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Vínculo sócio/plano não encontrado")
                );

        validarAcessoAoSocio(socioPlano.getSocio(), usuarioLogado);

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

    public PagamentoResponseDTO quitar(Long pagamentoId, PagamentoQuitarDTO dto, UsuarioModel usuarioLogado) {
        PagamentoModel pagamento = buscarPagamentoDoUsuario(pagamentoId, usuarioLogado);

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

    public PagamentoResponseDTO ajustar(
            Long pagamentoId,
            PagamentoAjusteDTO dto,
            UsuarioModel usuarioLogado
    ) {
        PagamentoModel pagamento = buscarPagamentoDoUsuario(pagamentoId, usuarioLogado);

        if (pagamento.getStatus() == StatusPagamento.PAGO) {
            throw new IllegalStateException(
                    "Não é possível ajustar um pagamento já quitado"
            );
        }

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

    public List<PagamentoResumoDTO> listarPorSocio(
            Long socioId,
            UsuarioModel usuarioLogado
    ) {
        SocioModel socio = socioRepository.findById(socioId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Sócio não encontrado")
                );

        validarAcessoAoSocio(socio, usuarioLogado);

        return pagamentoRepository
                .findBySocioPlano_Socio_IdAndSocioPlano_Socio_Clube_Id(
                        socioId,
                        socio.getClube().getId()
                )
                .stream()
                .map(pagamentoMapper::toResumoDTO)
                .toList();
    }

    private PagamentoModel buscarPagamentoDoUsuario(Long pagamentoId, UsuarioModel usuarioLogado) {
        PagamentoModel pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Pagamento não encontrado")
                );

        validarAcessoAoSocio(pagamento.getSocioPlano().getSocio(), usuarioLogado);
        return pagamento;
    }

    private void validarAcessoAoSocio(SocioModel socio, UsuarioModel usuarioLogado) {
        if (!socio.getClube().getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Registro não pertence aos seus clubes");
        }
    }

    public List<InadimplenteDTO> gerarRelatorioInadimplentes(
            YearMonth competencia,
            Long clubeId,
            UsuarioModel usuarioLogado
    ) {
        var clube = clubeRepository.findById(clubeId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Clube não encontrado")
                );

        if (!clube.getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este clube");
        }

        return pagamentoRepository
                .buscarInadimplentes(
                        competencia,
                        clube.getId()
                )
                .stream()
                .map(pagamentoMapper::toInadimplente)
                .toList();
    }
}