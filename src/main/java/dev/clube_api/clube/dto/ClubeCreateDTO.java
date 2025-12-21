package dev.clube_api.clube.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubeCreateDTO {
    @NotBlank(message = "Nome do clube é obrigatório")
    private String nome;

    @NotBlank(message = "CNPJ do clube é obrigatório")
    @Size(min=14, max=14, message = "CNPJ deve ter 14 caracteres")
    @Pattern(regexp = "\\d+", message = "CNPJ deve conter apenas números")
    private String cnpj;
}
