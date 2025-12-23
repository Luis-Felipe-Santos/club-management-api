package dev.clube_api.socio.dto;

import dev.clube_api.socio.enums.StatusSocio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioResumoDTO {
    private Long id;
    private String nome;
    private String documento;
    private String telefone;
    private StatusSocio status;
}
