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
import dev.clube_api.socio_plano_historico.enums.AcaoSocioPlano;
import dev.clube_api.socio_plano_historico.service.SocioPlanoHistoricoService;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.stereotype.Service;
import dev.clube_api.socio_plano.repository.SocioPlanoRepository;

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

        if (!socio.getClube().getId().equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Sócio não pertence ao seu clube");
        }

        if (socioPlanoRepository.existsBySocioAndPlanoAndStatus(socio, plano, StatusSocioPlano.ATIVO)) {
            throw new IllegalStateException("Sócio já está ativo neste plano");
        }

        SocioPlanoModel sp = socioPlanoMapper.toEntity(socio, plano);
        sp.setStatus(StatusSocioPlano.ATIVO);

        SocioPlanoModel salvo = socioPlanoRepository.save(sp);

        historicoService.registrar(salvo, null, StatusSocioPlano.ATIVO, AcaoSocioPlano.VINCULO, usuarioLogado);
        return socioPlanoMapper.toResponseDTO(salvo);
    }

    public List<SocioPlanoResumoDTO> listarPorSocio(Long socioId, UsuarioModel usuarioLogado) {
        SocioModel socio = socioRepository.findById(socioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sócio não encontrado"));

        if (!socio.getClube().getId().equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Sócio não pertence ao seu clube");
        }

        return socioPlanoRepository.findBySocio(socio)
                .stream()
                .map(socioPlanoMapper::toResumoDTO)
                .toList();
    }

    public List<SocioPlanoResumoDTO> listarPorSocioResumo(
            SocioModel socio,
            UsuarioModel usuarioLogado
    ) {
        if (!socio.getClube().getId().equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Sócio não pertence ao seu clube");
        }

        return socioPlanoRepository.findBySocio(socio)
                .stream()
                .map(socioPlanoMapper::toResumoDTO)
                .toList();
    }

    public List<SocioResumoDTO> listarSociosPorPlano(Long planoId, UsuarioModel usuarioLogado) {
        PlanoModel plano = planoRepository.findById(planoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado"));

        if (!plano.getClube().getId().equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Plano não pertence ao seu clube");
        }

        return socioPlanoRepository
                .findByPlanoAndStatusAndSocio_Clube_Id(
                        plano,
                        StatusSocioPlano.ATIVO,
                        usuarioLogado.getClube().getId()
                )
                .stream()
                .map(sp -> socioMapper.toResumoDTO(sp.getSocio()))
                .toList();
    }


        private SocioPlanoModel buscarVinculoDoClube(Long socioPlanoId, UsuarioModel usuarioLogado) {
        SocioPlanoModel sp = socioPlanoRepository.findById(socioPlanoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Vínculo não encontrado")
                );

        if (!sp.getSocio().getClube().getId()
                .equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Vínculo não pertence ao seu clube");
        }

        return sp;
    }
    public SocioPlanoResponseDTO suspender(Long socioPlanoId, UsuarioModel usuarioLogado) {
        SocioPlanoModel sp =
                buscarVinculoDoClube(socioPlanoId, usuarioLogado);

        if (sp.getStatus() != StatusSocioPlano.ATIVO) {
            throw new IllegalStateException(
                    "Só é possível suspender um plano ATIVO"
            );
        }

        StatusSocioPlano statusAnterior = sp.getStatus();

        sp.setStatus(StatusSocioPlano.SUSPENSO);

        SocioPlanoModel salvo = socioPlanoRepository.save(sp);

        historicoService.registrar(salvo, statusAnterior, StatusSocioPlano.SUSPENSO, AcaoSocioPlano.SUSPENSAO, usuarioLogado);

        return socioPlanoMapper.toResponseDTO(salvo);
    }
    public SocioPlanoResponseDTO cancelar(Long socioPlanoId, UsuarioModel usuarioLogado) {
        SocioPlanoModel sp = buscarVinculoDoClube(socioPlanoId, usuarioLogado);

        if (sp.getStatus() == StatusSocioPlano.CANCELADO) {
            throw new IllegalStateException(
                    "Plano já está cancelado"
            );
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
        SocioPlanoModel sp = buscarVinculoDoClube(socioPlanoId, usuarioLogado);

        if (sp.getStatus() != StatusSocioPlano.SUSPENSO) {
            throw new IllegalStateException(
                    "Só é possível reativar planos SUSPENSOS"
            );
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

        SocioPlanoModel atual =
                buscarVinculoDoClube(socioPlanoId, usuarioLogado);

        if (atual.getStatus() != StatusSocioPlano.ATIVO) {
            throw new IllegalStateException(
                    "Só é possível alterar um plano ATIVO"
            );
        }

        PlanoModel novoPlano = planoRepository.findById(novoPlanoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Novo plano não encontrado")
                );

        if (!novoPlano.getClube().getId()
                .equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Plano não pertence ao seu clube");
        }

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

        SocioPlanoModel novoVinculo =
                socioPlanoMapper.toEntity(atual.getSocio(), novoPlano);

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


}
