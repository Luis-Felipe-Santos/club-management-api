package dev.clube_api.usuario.dto;

import dev.clube_api.usuario.enums.RoleUsuario;
import dev.clube_api.usuario.enums.StatusUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAdminUpdateDTO {
    private RoleUsuario roleUsuario;
    private StatusUsuario statusUsuario;
}
