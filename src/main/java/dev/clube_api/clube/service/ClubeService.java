package dev.clube_api.clube.service;

import dev.clube_api.clube.dto.ClubeCreateDTO;
import dev.clube_api.clube.dto.ClubeResponseDTO;
import dev.clube_api.clube.dto.ClubeUpdateDTO;
import dev.clube_api.clube.enums.StatusClube;
import dev.clube_api.clube.mapper.ClubeMapper;
import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.clube.repository.ClubeRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final ClubeMapper clubeMapper;

    public ClubeService(ClubeRepository clubeRepository, ClubeMapper clubeMapper) {
        this.clubeRepository = clubeRepository;
        this.clubeMapper = clubeMapper;
    }

    public ClubeResponseDTO criarClube(ClubeCreateDTO dto, UsuarioModel usuarioLogado) {

        if (clubeRepository.existsByCnpj(dto.getCnpj())) {
            throw new IllegalArgumentException("Já existe um clube com este CNPJ");
        }

        ClubeModel clube = clubeMapper.toEntity(dto);
        clube.setAdmin(usuarioLogado);
        clube.setStatus(StatusClube.ATIVO);
        clube.setDataCadastro(LocalDateTime.now());

        ClubeModel salvo = clubeRepository.save(clube);

        return clubeMapper.toResponseDTO(salvo);
    }

    public ClubeResponseDTO buscarPorId(Long id, UsuarioModel usuarioLogado) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));

        if (!clube.getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este clube");
        }

        return clubeMapper.toResponseDTO(clube);
    }

    public List<ClubeResponseDTO> listarPorUsuario(UsuarioModel usuarioLogado) {
        return clubeRepository.findByAdmin(usuarioLogado)
                .stream()
                .map(clubeMapper::toResponseDTO)
                .toList();
    }

    public ClubeResponseDTO atualizar(Long id, ClubeUpdateDTO dto, UsuarioModel usuarioLogado) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));

        if (!clube.getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este clube");
        }

        clubeMapper.updateEntity(clube, dto);

        ClubeModel atualizado = clubeRepository.save(clube);
        return clubeMapper.toResponseDTO(atualizado);
    }

    public void inativar(Long id, UsuarioModel usuarioLogado) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));

        if (!clube.getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este clube");
        }

        clube.setStatus(StatusClube.INATIVO);
        clubeRepository.save(clube);
    }

    public void bloquear(Long id, UsuarioModel usuarioLogado) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));

        if (!clube.getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este clube");
        }

        clube.setStatus(StatusClube.BLOQUEADO);
        clubeRepository.save(clube);
    }

    public void reativar(Long id, UsuarioModel usuarioLogado) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));

        if (!clube.getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este clube");
        }

        clube.setStatus(StatusClube.ATIVO);
        clubeRepository.save(clube);
    }
}