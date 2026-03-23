package dev.clube_api.socio_plano.service;

import dev.clube_api.plano.model.PlanoModel;
import dev.clube_api.plano.repository.PlanoRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.socio.dto.SocioResumoDTO;
import dev.clube_api.socio.mapper.SocioMapper;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio.repository.SocioRepository;
import dev.clube_api.socio_plano.dto.SocioPlanoCreateDTO;
import dev.clube_api.socio_plano.dto.SocioPlanoResponseDTO;
import dev.clube_api.socio_plano.dto.SocioPlanoResumoDTO;
import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import dev.clube_api.socio_plano.mapper.SocioPlanoMapper;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import dev.clube_api.socio_plano.repository.SocioPlanoRepository;
import dev.clube_api.socio_plano_historico.enums.AcaoSocioPlano;
import dev.clube_api.socio_plano_historico.service.SocioPlanoHistoricoService;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioPlanoService {

    private final SocioPlanoRepository socioPlanoRepository;
    private final SocioPlanoMapper socioPlanoMapper;
    private final SocioRepository socioRepository;
    private final PlanoRepository planoRepository;
    private final SocioPlanoHistoricoService historicoService;
    private final SocioMapper socioMapper;

    public SocioPlanoService(
            SocioPlanoRepository socioPlanoRepository,
            SocioPlanoMapper socioPlanoMapper,
            SocioRepository socioRepository,
            PlanoRepository planoRepository,
            SocioPlanoHistoricoService historicoService,
            SocioMapper socioMapper
    ) {
        this.socioPlanoRepository = socioPlanoRepository;
        this.socioPlanoMapper = socioPlanoMapper;
        this.socioRepository = socioRepository;
        this.planoRepository = planoRepository;
        this.historicoService = historicoService;
        this.socioMapper = socioMapper;
    }

    public SocioPlanoResponseDTO vincular(SocioPlanoCreateDTO dto, UsuarioModel usuarioLogado) {
        SocioModel socio = socioRepository.findById(dto.getSocioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sócio não encontrado"));

        PlanoModel plano = planoRepository.findById(dto.getPlanoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado"));

        validarMesmoClube(socio, plano);
        validarAcessoAoClube(socio.getClube().getAdmin().getId(), usuarioLogado);

        if (socioPlanoRepository.existsBySocioAndPlanoAndStatus(socio, plano, StatusSocioPlano.ATIVO)) {
            throw new IllegalStateException("Sócio já está ativo neste plano");
        }

        SocioPlanoModel sp = socioPlanoMapper.toEntity(socio, plano);
        sp.setStatus(StatusSocioPlano.ATIVO);

        SocioPlanoModel salvo = socioPlanoRepository.save(sp);

        historicoService.registrar(
                salvo,
                null,
                StatusSocioPlano.ATIVO,
                AcaoSocioPlano.VINCULO,
                usuarioLogado
        );

        return socioPlanoMapper.toResponseDTO(salvo);
    }

    public List<SocioPlanoResumoDTO> listarPorSocio(Long socioId, UsuarioModel usuarioLogado) {
        SocioModel socio = socioRepository.findById(socioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sócio não encontrado"));

        validarAcessoAoClube(socio.getClube().getAdmin().getId(), usuarioLogado);

        return socioPlanoRepository.findBySocio(socio)
                .stream()
                .map(socioPlanoMapper::toResumoDTO)
                .toList();
    }

    public List<SocioPlanoResumoDTO> listarPorSocioResumo(
            SocioModel socio,
            UsuarioModel usuarioLogado
    ) {
        validarAcessoAoClube(socio.getClube().getAdmin().getId(), usuarioLogado);

        return socioPlanoRepository.findBySocio(socio)
                .stream()
                .map(socioPlanoMapper::toResumoDTO)
                .toList();
    }

    public List<SocioResumoDTO> listarSociosPorPlano(Long planoId, UsuarioModel usuarioLogado) {
        PlanoModel plano = planoRepository.findById(planoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado"));

        validarAcessoAoClube(plano.getClube().getAdmin().getId(), usuarioLogado);

        return socioPlanoRepository
                .findByPlanoAndStatusAndSocio_Clube_Id(
                        plano,
                        StatusSocioPlano.ATIVO,
                        plano.getClube().getId()
                )
                .stream()
                .map(sp -> socioMapper.toResumoDTO(sp.getSocio()))
                .toList();
    }

    public SocioPlanoResponseDTO suspender(Long socioPlanoId, UsuarioModel usuarioLogado) {
        SocioPlanoModel sp = buscarVinculoDoUsuario(socioPlanoId, usuarioLogado);

        if (sp.getStatus() != StatusSocioPlano.ATIVO) {
            throw new IllegalStateException("Só é possível suspender um plano ATIVO");
        }

        StatusSocioPlano statusAnterior = sp.getStatus();

        sp.setStatus(StatusSocioPlano.SUSPENSO);

        SocioPlanoModel salvo = socioPlanoRepository.save(sp);

        historicoService.registrar(
                salvo,
                statusAnterior,
                StatusSocioPlano.SUSPENSO,
                AcaoSocioPlano.SUSPENSAO,
                usuarioLogado
        );

        return socioPlanoMapper.toResponseDTO(salvo);
    }

    public SocioPlanoResponseDTO cancelar(Long socioPlanoId, UsuarioModel usuarioLogado) {
        SocioPlanoModel sp = buscarVinculoDoUsuario(socioPlanoId, usuarioLogado);

        if (sp.getStatus() == StatusSocioPlano.CANCELADO) {
            throw new IllegalStateException("Plano já está cancelado");
        }

        StatusSocioPlano statusAnterior = sp.getStatus();

        sp.setStatus(StatusSocioPlano.CANCELADO);

        SocioPlanoModel salvo = socioPlanoRepository.save(sp);

        historicoService.registrar(
                salvo,
                statusAnterior,
                StatusSocioPlano.CANCELADO,
                AcaoSocioPlano.CANCELAMENTO,
                usuarioLogado
        );

        return socioPlanoMapper.toResponseDTO(salvo);
    }

    public SocioPlanoResponseDTO reativar(Long socioPlanoId, UsuarioModel usuarioLogado) {
        SocioPlanoModel sp = buscarVinculoDoUsuario(socioPlanoId, usuarioLogado);

        if (sp.getStatus() != StatusSocioPlano.SUSPENSO) {
            throw new IllegalStateException("Só é possível reativar planos SUSPENSOS");
        }

        StatusSocioPlano statusAnterior = sp.getStatus();

        sp.setStatus(StatusSocioPlano.ATIVO);

        SocioPlanoModel salvo = socioPlanoRepository.save(sp);

        historicoService.registrar(
                salvo,
                statusAnterior,
                StatusSocioPlano.ATIVO,
                AcaoSocioPlano.REATIVACAO,
                usuarioLogado
        );

        return socioPlanoMapper.toResponseDTO(salvo);
    }

    public SocioPlanoResponseDTO alterarPlano(Long socioPlanoId, Long novoPlanoId, UsuarioModel usuarioLogado) {
        SocioPlanoModel atual = buscarVinculoDoUsuario(socioPlanoId, usuarioLogado);

        if (atual.getStatus() != StatusSocioPlano.ATIVO) {
            throw new IllegalStateException("Só é possível alterar um plano ATIVO");
        }

        PlanoModel novoPlano = planoRepository.findById(novoPlanoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Novo plano não encontrado"));

        if (!novoPlano.getClube().getId().equals(atual.getSocio().getClube().getId())) {
            throw new IllegalArgumentException("O novo plano deve pertencer ao mesmo clube do sócio");
        }

        validarAcessoAoClube(novoPlano.getClube().getAdmin().getId(), usuarioLogado);

        StatusSocioPlano statusAnterior = atual.getStatus();
        atual.setStatus(StatusSocioPlano.CANCELADO);
        socioPlanoRepository.save(atual);

        historicoService.registrar(
                atual,
                statusAnterior,
                StatusSocioPlano.CANCELADO,
                AcaoSocioPlano.ALTERACAO,
                usuarioLogado
        );

        SocioPlanoModel novoVinculo = socioPlanoMapper.toEntity(atual.getSocio(), novoPlano);
        novoVinculo.setStatus(StatusSocioPlano.ATIVO);

        SocioPlanoModel salvo = socioPlanoRepository.save(novoVinculo);

        historicoService.registrar(
                salvo,
                null,
                StatusSocioPlano.ATIVO,
                AcaoSocioPlano.ALTERACAO,
                usuarioLogado
        );

        return socioPlanoMapper.toResponseDTO(salvo);
    }

    private SocioPlanoModel buscarVinculoDoUsuario(Long socioPlanoId, UsuarioModel usuarioLogado) {
        SocioPlanoModel sp = socioPlanoRepository.findById(socioPlanoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Vínculo não encontrado")
                );

        validarAcessoAoClube(sp.getSocio().getClube().getAdmin().getId(), usuarioLogado);

        return sp;
    }

    private void validarAcessoAoClube(Long adminIdDoClube, UsuarioModel usuarioLogado) {
        if (!adminIdDoClube.equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este clube");
        }
    }

    private void validarMesmoClube(SocioModel socio, PlanoModel plano) {
        if (!socio.getClube().getId().equals(plano.getClube().getId())) {
            throw new IllegalArgumentException("Sócio e plano devem pertencer ao mesmo clube");
        }
    }
}