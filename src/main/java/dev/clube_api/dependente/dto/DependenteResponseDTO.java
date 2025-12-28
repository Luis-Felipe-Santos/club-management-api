package dev.clube_api.dependente.dto;

import dev.clube_api.dependente.enums.StatusDependente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependenteResponseDTO {

    private Long id;

    private  String nome;
    private String parentesco;
    private String imagemUrl;

    private StatusDependente status;

    private Long socioId;
    private String nomeSocio;

    private LocalDateTime created_At;
    private LocalDateTime updated_At;

}
