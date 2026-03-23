package dev.clube_api.usuario.dto;


import dev.clube_api.usuario.enums.RoleUsuario;
import dev.clube_api.usuario.enums.StatusUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String imagemUrl;

    private RoleUsuario role;
    private StatusUsuario status;
}
