package dev.clube_api.usuario.service;

import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.clube.repository.ClubeRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.usuario.dto.*;
import dev.clube_api.usuario.enums.StatusUsuario;
import dev.clube_api.usuario.mapper.UsuarioMapper;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClubeRepository clubeRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, ClubeRepository clubeRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.clubeRepository = clubeRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDTO criar(UsuarioCreateDTO dto, UsuarioModel usuarioLogado){
        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        UsuarioModel usuario = usuarioMapper.toEntity(
                dto,
                usuarioLogado.getClube()
        );

        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        UsuarioModel salvo = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(salvo);
    }

    public List<UsuarioResponseDTO> listarPorClube(UsuarioModel usuarioLogado) {

        if (usuarioLogado.getClube() == null) {
            throw new SecurityException("Usuário não está vinculado a um clube");
        }

        return usuarioRepository.findByClube(usuarioLogado.getClube())
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    public UsuarioModel buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado")
                );
    }

    public UsuarioResponseDTO buscarPorId(
            Long id,
            UsuarioModel usuarioLogado
    ) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado")
                );

        if (!usuario.getClube().getId()
                .equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Usuário não pertence ao seu clube");
        }

        return usuarioMapper.toResponseDTO(usuario);
    }




    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateDTO dto){
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        usuarioMapper.updateModel(dto, usuario);

        UsuarioModel atualizado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(atualizado);

    }
    public UsuarioModel buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado")
                );
    }
    public void atualizarSenha(Long id, UsuarioSenhaUpdateDTO dto) {

        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));

        usuarioRepository.save(usuario);
    }
    public UsuarioResponseDTO atualizarPermissoes(
            Long id,
            UsuarioAdminUpdateDTO dto
    ) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (dto.getRoleUsuario() != null) {
            usuario.setRole(dto.getRoleUsuario());
        }

        if (dto.getStatusUsuario() != null) {
            usuario.setStatus(dto.getStatusUsuario());
        }

        UsuarioModel atualizado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(atualizado);
    }

    public void inativar(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        usuario.setStatus(StatusUsuario.INATIVO);
        usuarioRepository.save(usuario);

    }
    public void bloquear(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        usuario.setStatus(StatusUsuario.BLOQUEADO);
        usuarioRepository.save(usuario);

    }
    public void desbloquear(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        usuario.setStatus(StatusUsuario.ATIVO);
        usuarioRepository.save(usuario);
    }

}
