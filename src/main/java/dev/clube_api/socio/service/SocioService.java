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
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioService {

    private final SocioRepository socioRepository;
    private final SocioMapper socioMapper;

    public SocioService(SocioRepository socioRepository, SocioMapper socioMapper) {
        this.socioRepository = socioRepository;
        this.socioMapper = socioMapper;
    }

    public SocioResponseDTO criar(SocioCreateDTO dto, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        SocioModel socio =
                socioMapper.toEntity(dto, usuarioLogado.getClube());

        socio.setStatus(StatusSocio.ATIVO);

        return socioMapper.toResponseDTO(
                socioRepository.save(socio)
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

        return socioMapper.toResponseDTO(socio);
    }

    public SocioResponseDTO atualizar(Long socioId, SocioUpdateDTO dto, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        SocioModel socio =
                buscarSocioDoClube(socioId, usuarioLogado);

        socioMapper.updateEntity(socio, dto);

        return socioMapper.toResponseDTO(
                socioRepository.save(socio)
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

