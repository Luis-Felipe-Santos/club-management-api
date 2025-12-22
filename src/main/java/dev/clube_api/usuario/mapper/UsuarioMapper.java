package dev.clube_api.usuario.mapper;

import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.usuario.dto.UsuarioCreateDTO;
import dev.clube_api.usuario.dto.UsuarioResponseDTO;
import dev.clube_api.usuario.dto.UsuarioUpdateDTO;
import dev.clube_api.usuario.enums.StatusUsuario;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioModel toEntity(UsuarioCreateDTO dto, ClubeModel clube){
        UsuarioModel usuario = new UsuarioModel();
        usuario.setImagemUrl(dto.getImagemURL());
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setRole(dto.getRole());
        usuario.setStatus(StatusUsuario.ATIVO);
        usuario.setClube(clube);

        return usuario;
    }


    public void updateModel(UsuarioUpdateDTO dto, UsuarioModel usuario) {

        if (dto.getImagemUrl() != null) {
            usuario.setImagemUrl(dto.getImagemUrl());
        }

        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }

        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }

    }

    public UsuarioResponseDTO toResponseDTO(UsuarioModel usuarioModel) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();

        dto.setId(usuarioModel.getId());
        dto.setImagemUrl(usuarioModel.getImagemUrl());
        dto.setNome(usuarioModel.getNome());
        dto.setCpf(usuarioModel.getCpf());
        dto.setEmail(usuarioModel.getEmail());
        dto.setRole(usuarioModel.getRole());
        dto.setStatus(usuarioModel.getStatus());

        if (usuarioModel.getClube() != null) {
            dto.setClubeId(usuarioModel.getClube().getId());
            dto.setNomeClube(usuarioModel.getClube().getNome());
        }

        return dto;
    }

}
