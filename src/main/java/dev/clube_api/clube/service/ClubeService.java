package dev.clube_api.clube.service;


import dev.clube_api.clube.dto.ClubeCreateDTO;
import dev.clube_api.clube.dto.ClubeResponseDTO;
import dev.clube_api.clube.dto.ClubeUpdateDTO;
import dev.clube_api.clube.mapper.ClubeMapper;
import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.clube.repository.ClubeRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.usuario.enums.RoleUsuario;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.repository.UsuarioRepository;
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
        ClubeModel clube = clubeMapper.toEntity(dto);
        clube.setAdmin(usuarioLogado);

        ClubeModel salvo = clubeRepository.save(clube);
        usuarioLogado.setClube(salvo);
        usuarioLogado.setRole(RoleUsuario.ADMIN);

        usuarioRepository.save(usuarioLogado);

        return clubeMapper.toResponseDTO(salvo);
    }

    public ClubeResponseDTO buscarPorId(Long id) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));
        return clubeMapper.toResponseDTO(clube);
    }

    public List<ClubeResponseDTO> listar() {
        return clubeRepository.findAll()
                .stream()
                .map(clubeMapper::toResponseDTO)
                .toList();
    }

    public ClubeResponseDTO atualizar(Long id, ClubeUpdateDTO dto) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clube não encontrado"));

        clubeMapper.updateEntity(clube, dto);

        ClubeModel atualizado = clubeRepository.save(clube);
        return clubeMapper.toResponseDTO(atualizado);
    }
    public void deletar(Long id) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Clube não encontrado")
                );

        clubeRepository.delete(clube);
    }


}
