package dev.clube_api.plano.service;

import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.clube.repository.ClubeRepository;
import dev.clube_api.plano.dto.PlanoCreateDTO;
import dev.clube_api.plano.dto.PlanoResponseDTO;
import dev.clube_api.plano.dto.PlanoUpdateDTO;
import dev.clube_api.plano.enums.StatusPlano;
import dev.clube_api.plano.mapper.PlanoMapper;
import dev.clube_api.plano.model.PlanoModel;
import dev.clube_api.plano.repository.PlanoRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;
    private final PlanoMapper planoMapper;
    private final ClubeRepository clubeRepository;

    public PlanoService(PlanoRepository planoRepository, PlanoMapper planoMapper, ClubeRepository clubeRepository) {
        this.planoRepository = planoRepository;
        this.planoMapper = planoMapper;
        this.clubeRepository = clubeRepository;
    }

    public PlanoResponseDTO criar(
            PlanoCreateDTO dto,
            UsuarioModel usuarioLogado
    ) {
        ClubeModel clube = buscarClubeDoAdmin(dto.getClubeId(), usuarioLogado);

        if (planoRepository.existsByNomeAndClube(dto.getNome(), clube)) {
            throw new IllegalArgumentException(
                    "Já existe um plano com esse nome neste clube"
            );
        }

        PlanoModel plano = planoMapper.toEntity(dto, clube);
        plano.setStatus(StatusPlano.ATIVO);

        PlanoModel salvo = planoRepository.save(plano);
        return planoMapper.toResponseDTO(salvo);
    }

    public List<PlanoResponseDTO> listarPorClube(
            Long clubeId,
            UsuarioModel usuarioLogado
    ) {
        ClubeModel clube = buscarClubeDoAdmin(clubeId, usuarioLogado);

        return planoRepository
                .findByClube(clube)
                .stream()
                .map(planoMapper::toResponseDTO)
                .toList();
    }

    public PlanoResponseDTO atualizar(
            Long planoId,
            PlanoUpdateDTO dto,
            UsuarioModel usuarioLogado
    ) {
        PlanoModel plano = buscarPlanoDoAdmin(planoId, usuarioLogado);

        if (dto.getNome() != null
                && !dto.getNome().equals(plano.getNome())
                && planoRepository.existsByNomeAndClube(
                dto.getNome(),
                plano.getClube()
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
        PlanoModel plano = buscarPlanoDoAdmin(planoId, usuarioLogado);
        plano.setStatus(StatusPlano.INATIVO);
        planoRepository.save(plano);
    }

    public void reativar(Long planoId, UsuarioModel usuarioLogado) {
        PlanoModel plano = buscarPlanoDoAdmin(planoId, usuarioLogado);
        plano.setStatus(StatusPlano.ATIVO);
        planoRepository.save(plano);
    }


    private ClubeModel buscarClubeDoAdmin(Long clubeId, UsuarioModel usuarioLogado) {
        if (clubeId == null) {
            throw new IllegalArgumentException("Clube é obrigatório");
        }

        ClubeModel clube = clubeRepository.findById(clubeId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Clube não encontrado")
                );

        if (!clube.getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este clube");
        }

        return clube;
    }

    private PlanoModel buscarPlanoDoAdmin(
            Long planoId,
            UsuarioModel usuarioLogado
    ) {
        PlanoModel plano = planoRepository.findById(planoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Plano não encontrado")
                );

        if (!plano.getClube().getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Plano não pertence aos seus clubes");
        }

        return plano;
    }
}

