package dev.clube_api.socio.service;


import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.socio.dto.SocioCreateDTO;
import dev.clube_api.socio.dto.SocioResponseDTO;
import dev.clube_api.socio.dto.SocioResumoDTO;
import dev.clube_api.socio.dto.SocioUpdateDTO;
import dev.clube_api.socio.enums.StatusSocio;
import dev.clube_api.socio.mapper.SocioMapper;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio.repository.SocioRepository;
import dev.clube_api.socio_plano.dto.SocioPlanoResumoDTO;
import dev.clube_api.socio_plano.service.SocioPlanoService;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioService {

    private final SocioRepository socioRepository;
    private final SocioMapper socioMapper;
    private final SocioPlanoService socioPlanoService;

    public SocioService(SocioRepository socioRepository, SocioMapper socioMapper, SocioPlanoService socioPlanoService) {
        this.socioRepository = socioRepository;
        this.socioMapper = socioMapper;
        this.socioPlanoService = socioPlanoService;

    }

    public SocioResponseDTO criar(SocioCreateDTO dto, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        SocioModel socio =
                socioMapper.toEntity(dto, usuarioLogado.getClube());

        socio.setStatus(StatusSocio.ATIVO);

        return socioMapper.toResponseDTO(
                socioRepository.save(socio),
                List.of()
        );
    }

    public List<SocioResumoDTO> listarPorClube(UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        return socioRepository
                .findByClube(usuarioLogado.getClube())
                .stream()
                .map(socioMapper::toResumoDTO)
                .toList();
    }

    public SocioResponseDTO buscarPorId(Long socioId, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        SocioModel socio = buscarSocioDoClube(socioId, usuarioLogado);

        List<SocioPlanoResumoDTO> planos = socioPlanoService.listarPorSocioResumo(socio, usuarioLogado);

        return socioMapper.toResponseDTO(socio, planos);
    }

    public SocioResponseDTO atualizar(Long socioId, SocioUpdateDTO dto, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        SocioModel socio =
                buscarSocioDoClube(socioId, usuarioLogado);

        socioMapper.updateEntity(socio, dto);

        var planos = socioPlanoService.listarPorSocioResumo(socio, usuarioLogado);


        return socioMapper.toResponseDTO(
                socioRepository.save(socio),
                planos
        );
    }

    public void inativar(Long socioId, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        SocioModel socio =
                buscarSocioDoClube(socioId, usuarioLogado);

        socio.setStatus(StatusSocio.INATIVO);
        socioRepository.save(socio);
    }

    public void bloquear(Long socioId, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        SocioModel socio =
                buscarSocioDoClube(socioId, usuarioLogado);

        socio.setStatus(StatusSocio.BLOQUEADO);
        socioRepository.save(socio);
    }

    public void reativar(Long socioId, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        SocioModel socio =
                buscarSocioDoClube(socioId, usuarioLogado);

        socio.setStatus(StatusSocio.ATIVO);
        socioRepository.save(socio);
    }

    private void validarUsuarioComClube(UsuarioModel usuarioLogado) {
        if (usuarioLogado.getClube() == null) {
            throw new SecurityException(
                    "Usuário não está vinculado a um clube"
            );
        }
    }

    private SocioModel buscarSocioDoClube(Long socioId, UsuarioModel usuarioLogado) {
        SocioModel socio = socioRepository.findById(socioId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Sócio não encontrado")
                );

        if (!socio.getClube().getId()
                .equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException(
                    "Sócio não pertence ao seu clube"
            );
        }

        return socio;
    }
}

