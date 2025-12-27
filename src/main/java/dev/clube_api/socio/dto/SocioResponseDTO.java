package dev.clube_api.socio.dto;


import dev.clube_api.socio.enums.StatusSocio;
import dev.clube_api.socio.enums.TipoDocumentoSocio;
import dev.clube_api.socio_plano.dto.SocioPlanoResumoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioResponseDTO {
    private Long id;
    private String nome;
    private TipoDocumentoSocio tipoDocumento;
    private String documento;
    private String telefone;
    private String email;
    private String endereco;
    private String imagemUrl;
    private StatusSocio status;
    private List<SocioPlanoResumoDTO> planos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
