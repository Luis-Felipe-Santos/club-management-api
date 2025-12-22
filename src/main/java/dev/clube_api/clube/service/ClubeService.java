package dev.clube_api.clube.service;


import dev.clube_api.clube.dto.ClubeCreateDTO;
import dev.clube_api.clube.dto.ClubeResponseDTO;
import dev.clube_api.clube.dto.ClubeUpdateDTO;
import dev.clube_api.clube.enums.StatusClube;
import dev.clube_api.clube.mapper.ClubeMapper;
import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.clube.repository.ClubeRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.usuario.enums.RoleUsuario;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.repository.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final ClubeMapper clubeMapper;
    private final UsuarioRepository usuarioRepository;

    public ClubeService(ClubeRepository clubeRepository, ClubeMapper clubeMapper, UsuarioRepository usuarioRepository) {
        this.clubeRepository = clubeRepository;
        this.clubeMapper = clubeMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public ClubeResponseDTO criarClube(ClubeCreateDTO dto, UsuarioModel usuarioLogado) {
        if (usuarioLogado.getClube() != null) {
            throw new IllegalStateException("Usuário já pertence a um clube");
        }

        if (clubeRepository.existsByCnpj(dto.getCnpj())) {
            throw new IllegalArgumentException("Já existe um clube com este CNPJ");
        }

        ClubeModel clube = clubeMapper.toEntity(dto);
        clube.setAdmin(usuarioLogado);

        ClubeModel salvo = clubeRepository.save(clube);
        usuarioLogado.setClube(salvo);
        usuarioLogado.setRole(RoleUsuario.ADMIN);

        usuarioRepository.save(usuarioLogado);

        return clubeMapper.toResponseDTO(salvo);
    }

    public ClubeResponseDTO buscarPorId(Long id, UsuarioModel usuarioLogado) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));
        if (!clube.getId().equals(usuarioLogado.getClube().getId())) {
            throw new AccessDeniedException("Você não tem acesso a este clube");
        }
        return clubeMapper.toResponseDTO(clube);
    }

    public List<ClubeResponseDTO> listarPorUsuario(UsuarioModel usuarioLogado) {

        if (usuarioLogado.getClube() == null) {
            return List.of();
        }

        return List.of(
                clubeMapper.toResponseDTO(usuarioLogado.getClube())
        );
    }

    public ClubeResponseDTO atualizar(Long id, ClubeUpdateDTO dto) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));

        clubeMapper.updateEntity(clube, dto);

        ClubeModel atualizado = clubeRepository.save(clube);
        return clubeMapper.toResponseDTO(atualizado);
    }
    public void inativar(Long id) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));

        clube.setStatus(StatusClube.INATIVO);
        clubeRepository.save(clube);
    }
    public void bloquear(Long id) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));

        clube.setStatus(StatusClube.BLOQUEADO);
        clubeRepository.save(clube);
    }
    public void reativar(Long id) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));

        clube.setStatus(StatusClube.ATIVO);
        clubeRepository.save(clube);
    }





}
