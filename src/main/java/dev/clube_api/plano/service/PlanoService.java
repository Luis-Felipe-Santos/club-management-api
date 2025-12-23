package dev.clube_api.plano.service;

import dev.clube_api.plano.dto.PlanoCreateDTO;
import dev.clube_api.plano.dto.PlanoResponseDTO;
import dev.clube_api.plano.dto.PlanoUpdateDTO;
import dev.clube_api.plano.enums.StatusPlano;
import dev.clube_api.plano.mapper.PlanoMapper;
import dev.clube_api.plano.model.PlanoModel;
import dev.clube_api.plano.repository.PlanoRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;
    private final PlanoMapper planoMapper;

    public PlanoService(PlanoRepository planoRepository, PlanoMapper planoMapper) {
        this.planoRepository = planoRepository;
        this.planoMapper = planoMapper;
    }

    public PlanoResponseDTO criar(
            PlanoCreateDTO dto,
            UsuarioModel usuarioLogado
    ) {
        validarUsuarioComClube(usuarioLogado);

        if (planoRepository.existsByNomeAndClube(
                dto.getNome(),
                usuarioLogado.getClube()
        )) {
            throw new IllegalArgumentException(
                    "Já existe um plano com esse nome neste clube"
            );
        }

        PlanoModel plano =
                planoMapper.toEntity(dto, usuarioLogado.getClube());

        plano.setStatus(StatusPlano.ATIVO);

        PlanoModel salvo = planoRepository.save(plano);
        return planoMapper.toResponseDTO(salvo);
    }

    public List<PlanoResponseDTO> listarPorClube(
            UsuarioModel usuarioLogado
    ) {
        validarUsuarioComClube(usuarioLogado);

        return planoRepository
                .findByClube(usuarioLogado.getClube())
                .stream()
                .map(planoMapper::toResponseDTO)
                .toList();
    }

    public PlanoResponseDTO atualizar(
            Long planoId,
            PlanoUpdateDTO dto,
            UsuarioModel usuarioLogado
    ) {
        validarUsuarioComClube(usuarioLogado);

        PlanoModel plano = buscarPlanoDoClube(planoId, usuarioLogado);

        if (dto.getNome() != null
                && !dto.getNome().equals(plano.getNome())
                && planoRepository.existsByNomeAndClube(
                dto.getNome(),
                usuarioLogado.getClube()
        )) {
            throw new IllegalArgumentException(
                    "Já existe um plano com esse nome neste clube"
            );
        }

        planoMapper.updateEntity(plano, dto);

        PlanoModel atualizado = planoRepository.save(plano);
        return planoMapper.toResponseDTO(atualizado);
    }


    public void inativar(Long planoId, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        PlanoModel plano = buscarPlanoDoClube(planoId, usuarioLogado);

        plano.setStatus(StatusPlano.INATIVO);
        planoRepository.save(plano);
    }

    public void reativar(Long planoId, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        PlanoModel plano = buscarPlanoDoClube(planoId, usuarioLogado);

        plano.setStatus(StatusPlano.ATIVO);
        planoRepository.save(plano);
    }


    private void validarUsuarioComClube(UsuarioModel usuarioLogado) {
        if (usuarioLogado.getClube() == null) {
            throw new SecurityException(
                    "Usuário não está vinculado a um clube"
            );
        }
    }

    private PlanoModel buscarPlanoDoClube(
            Long planoId,
            UsuarioModel usuarioLogado
    ) {
        PlanoModel plano = planoRepository.findById(planoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Plano não encontrado")
                );

        if (!plano.getClube().getId()
                .equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException(
                    "Plano não pertence ao seu clube"
            );
        }

        return plano;
    }
}

