package dev.clube_api.socio.imagem.service;

import dev.clube_api.socio.imagem.dto.SignedUrlResponseDTO;
import dev.clube_api.socio.imagem.dto.UploadImagemResponseDTO;
import dev.clube_api.socio.imagem.supabase.SupabaseStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class SocioImagemService {

    private final SupabaseStorageService supabaseStorageService;

    public SocioImagemService(SupabaseStorageService supabaseStorageService) {
        this.supabaseStorageService = supabaseStorageService;
    }

    public UploadImagemResponseDTO uploadImagem(MultipartFile file, Long clubeId) {
        validarArquivo(file);

        String extensao = obterExtensao(file.getOriginalFilename());
        String nomeArquivo = UUID.randomUUID() + extensao;
        String path = "clubes/" + clubeId + "/socios/" + nomeArquivo;

        try {
            supabaseStorageService.uploadArquivo(file.getBytes(), path, file.getContentType());
            String signedUrl = supabaseStorageService.gerarSignedUrl(path, 3600);

            return new UploadImagemResponseDTO(path, signedUrl);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo enviado.");
        }
    }

    public SignedUrlResponseDTO gerarSignedUrl(String path) {
        String signedUrl = supabaseStorageService.gerarSignedUrl(path, 3600);
        return new SignedUrlResponseDTO(signedUrl);
    }

    private void validarArquivo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo é obrigatório.");
        }

        List<String> tiposPermitidos = List.of(
                "image/png",
                "image/jpeg",
                "image/jpg",
                "image/webp"
        );

        if (file.getContentType() == null || !tiposPermitidos.contains(file.getContentType())) {
            throw new IllegalArgumentException("Tipo de arquivo inválido. Envie PNG, JPG, JPEG ou WEBP.");
        }
    }

    private String obterExtensao(String nomeArquivo) {
        if (nomeArquivo == null || !nomeArquivo.contains(".")) {
            return ".jpg";
        }

        return nomeArquivo.substring(nomeArquivo.lastIndexOf("."));
    }
}