package dev.clube_api.dependente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependenteCreateDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String parentesco;

    private String imagemUrl;

    @NotNull
    private Long socioId;

}
