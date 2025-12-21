package dev.clube_api.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateDTO {
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private Long clubeId;
}
