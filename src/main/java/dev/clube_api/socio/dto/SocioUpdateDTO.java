package dev.clube_api.socio.dto;


import dev.clube_api.socio.enums.TipoDocumentoSocio;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioUpdateDTO {

    private String nome;

    private TipoDocumentoSocio tipoDocumento;


    @Size(min = 11, max = 14)
    private String documento;


    private String telefone;

    @Email
    private String email;

    private String endereco;

    private String imagemUrl;
}
