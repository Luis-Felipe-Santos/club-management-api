package dev.clube_api.socio.dto;

import dev.clube_api.socio.enums.StatusSocio;
import dev.clube_api.socio_plano.enums.StatusSocioPlano;
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
    private String email;
    private String imagemUrl;
    private String endereco;
    private StatusSocio status;

    private Long socioPlanoId;
    private Long planoId;
    private String planoNome;
    private StatusSocioPlano statusPlano;
    private Boolean possuiPlanoAtivo;
}