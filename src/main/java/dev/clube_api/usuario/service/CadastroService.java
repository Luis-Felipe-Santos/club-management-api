package dev.clube_api.usuario.service;

import dev.clube_api.usuario.dto.CadastroRequestDTO;
import dev.clube_api.usuario.dto.UsuarioResponseDTO;
import dev.clube_api.usuario.enums.RoleUsuario;
import dev.clube_api.usuario.enums.StatusUsuario;
import dev.clube_api.usuario.mapper.UsuarioMapper;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CadastroService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public CadastroService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            UsuarioMapper usuarioMapper
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    public UsuarioResponseDTO cadastrar(CadastroRequestDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setRole(RoleUsuario.ADMIN);
        usuario.setStatus(StatusUsuario.ATIVO);

        UsuarioModel salvo = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(salvo);
    }
}