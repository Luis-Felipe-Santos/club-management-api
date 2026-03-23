package dev.clube_api.socio.imagem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadImagemResponseDTO {
    private String path;
    private String signedUrl;
}