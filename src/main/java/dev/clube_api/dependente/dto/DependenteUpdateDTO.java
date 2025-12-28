package dev.clube_api.dependente.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DependenteUpdateDTO {

    private String nome;
    private String parentesco;
    private String imagemUrl;
}
