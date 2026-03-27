package dev.clube_api.pagamento.service;

import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.clube.repository.ClubeRepository;
import dev.clube_api.pagamento.dto.*;
import dev.clube_api.pagamento.enums.StatusPagamento;
import dev.clube_api.pagamento.mapper.PagamentoMapper;
import dev.clube_api.pagamento.model.PagamentoModel;
import dev.clube_api.pagamento.repository.PagamentoRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio.repository.SocioRepository;
import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import dev.clube_api.socio_plano.repository.SocioPlanoRepository;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public PagamentoResponseDTO quitar(
            Long pagamentoId,
            PagamentoQuitarDTO dto,
            UsuarioModel usuarioLogado
    ) {
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

        if (dto.getObservacao() != null) {
            pagamento.setObservacao(dto.getObservacao());
        }

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

        if (dto.getDataPagamento() != null) {
            pagamento.setDataPagamento(dto.getDataPagamento());
        }

        if (dto.getValorFinal() != null) {
            pagamento.setValorFinal(dto.getValorFinal());
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

    public List<PagamentoGradeLinhaDTO> listarGrade(
            Long clubeId,
            Integer ano,
            Long planoId,
            UsuarioModel usuarioLogado
    ) {
        ClubeModel clube = clubeRepository.findById(clubeId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Clube não encontrado")
                );

        validarAcessoAoClube(clube, usuarioLogado);

        List<SocioPlanoModel> socioPlanos = buscarSocioPlanosAtivosDaGrade(clubeId, planoId);

        if (socioPlanos.isEmpty()) {
            return List.of();
        }

        List<Long> socioPlanoIds = socioPlanos.stream()
                .map(SocioPlanoModel::getId)
                .toList();

        YearMonth inicio = YearMonth.of(ano, 1);
        YearMonth fim = YearMonth.of(ano, 12);

        List<PagamentoModel> pagamentos = pagamentoRepository
                .findBySocioPlano_IdInAndCompetenciaBetween(socioPlanoIds, inicio, fim);

        Map<String, PagamentoModel> pagamentosMap = indexarPagamentos(pagamentos);

        return socioPlanos.stream()
                .map(socioPlano -> montarLinhaGrade(socioPlano, ano, pagamentosMap))
                .toList();
    }
    public List<PagamentoListaDTO> listar(
            Long clubeId,
            Long planoId,
            YearMonth competencia,
            StatusPagamento status,
            String busca,
            UsuarioModel usuarioLogado
    ) {
        ClubeModel clube = clubeRepository.findById(clubeId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Clube não encontrado")
                );

        validarAcessoAoClube(clube, usuarioLogado);

        return pagamentoRepository
                .buscarComFiltros(clubeId, planoId, competencia, status, busca)
                .stream()
                .map(pagamentoMapper::toListaDTO)
                .toList();
    }

    public List<InadimplenteDTO> gerarRelatorioInadimplentes(
            YearMonth competencia,
            Long clubeId,
            UsuarioModel usuarioLogado
    ) {
        ClubeModel clube = clubeRepository.findById(clubeId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Clube não encontrado")
                );

        validarAcessoAoClube(clube, usuarioLogado);

        return pagamentoRepository
                .buscarInadimplentes(
                        competencia,
                        clube.getId()
                )
                .stream()
                .map(pagamentoMapper::toInadimplente)
                .toList();
    }

    private List<SocioPlanoModel> buscarSocioPlanosAtivosDaGrade(Long clubeId, Long planoId) {
        if (planoId != null) {
            return socioPlanoRepository.findBySocio_Clube_IdAndPlano_IdAndStatus(
                    clubeId,
                    planoId,
                    StatusSocioPlano.ATIVO
            );
        }

        return socioPlanoRepository.findBySocio_Clube_IdAndStatus(
                clubeId,
                StatusSocioPlano.ATIVO
        );
    }

    private Map<String, PagamentoModel> indexarPagamentos(List<PagamentoModel> pagamentos) {
        Map<String, PagamentoModel> mapa = new HashMap<>();

        for (PagamentoModel pagamento : pagamentos) {
            String chave = gerarChavePagamento(
                    pagamento.getSocioPlano().getId(),
                    pagamento.getCompetencia()
            );
            mapa.put(chave, pagamento);
        }

        return mapa;
    }

    private PagamentoGradeLinhaDTO montarLinhaGrade(
            SocioPlanoModel socioPlano,
            Integer ano,
            Map<String, PagamentoModel> pagamentosMap
    ) {
        List<PagamentoMesDTO> meses = java.util.stream.IntStream.rangeClosed(1, 12)
                .mapToObj(mes -> {
                    YearMonth competencia = YearMonth.of(ano, mes);

                    String chave = gerarChavePagamento(socioPlano.getId(), competencia);
                    PagamentoModel pagamento = pagamentosMap.get(chave);

                    if (pagamento == null) {
                        return new PagamentoMesDTO(
                                null,
                                competencia,
                                null,
                                null,
                                null,
                                null
                        );
                    }

                    return new PagamentoMesDTO(
                            pagamento.getId(),
                            pagamento.getCompetencia(),
                            pagamento.getValorFinal(),
                            pagamento.getStatus(),
                            pagamento.getDataVencimento(),
                            pagamento.getDataPagamento()
                    );
                })
                .toList();

        return new PagamentoGradeLinhaDTO(
                socioPlano.getSocio().getId(),
                socioPlano.getSocio().getNome(),
                socioPlano.getSocio().getImagemUrl(),
                socioPlano.getId(),
                socioPlano.getPlano().getId(),
                socioPlano.getPlano().getNome(),
                socioPlano.getPlano().getValor(),
                meses
        );
    }

    private String gerarChavePagamento(Long socioPlanoId, YearMonth competencia) {
        return socioPlanoId + "_" + competencia;
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

    private void validarAcessoAoClube(ClubeModel clube, UsuarioModel usuarioLogado) {
        if (!clube.getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este clube");
        }
    }
}