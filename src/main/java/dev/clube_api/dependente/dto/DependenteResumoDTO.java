package dev.clube_api.dependente.dto;


import dev.clube_api.dependente.enums.StatusDependente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependenteResumoDTO {

    private Long id;
    private String nome;
    private String parentesco;
    private StatusDependente status;
}
