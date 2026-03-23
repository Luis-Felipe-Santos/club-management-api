package dev.clube_api.clube.dto;


import dev.clube_api.clube.enums.StatusClube;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubeResponseDTO {
    private Long id;
    private String nome;
    private  String cnpj;
    private StatusClube status;
    private LocalDateTime dataCadastro;
    private Long usuarioAdminId;
    private String nomeAdmin;
}
