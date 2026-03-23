package dev.clube_api.socio.controller;

import dev.clube_api.socio.dto.SocioCreateDTO;
import dev.clube_api.socio.dto.SocioResponseDTO;
import dev.clube_api.socio.dto.SocioResumoDTO;
import dev.clube_api.socio.dto.SocioUpdateDTO;
import dev.clube_api.socio.imagem.dto.SignedUrlResponseDTO;
import dev.clube_api.socio.imagem.dto.UploadImagemResponseDTO;
import dev.clube_api.socio.imagem.service.SocioImagemService;
import dev.clube_api.socio.service.SocioService;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/socios")
public class SocioController {

    private final SocioService socioService;
    private final SocioImagemService socioImagemService;
    private final UsuarioService usuarioService;

    public SocioController(
            SocioService socioService,
            SocioImagemService socioImagemService,
            UsuarioService usuarioService
    ) {
        this.socioService = socioService;
        this.socioImagemService = socioImagemService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SocioResponseDTO> criar(
            @RequestBody @Valid SocioCreateDTO dto,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                socioService.criar(dto, usuarioLogado)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    public ResponseEntity<List<SocioResumoDTO>> listar(
            @RequestParam Long clubeId,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                socioService.listarPorClube(clubeId, usuarioLogado)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    public ResponseEntity<SocioResponseDTO> buscarPorId(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                socioService.buscarPorId(id, usuarioLogado)
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SocioResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid SocioUpdateDTO dto,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                socioService.atualizar(id, dto, usuarioLogado)
        );
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> inativar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        socioService.inativar(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/bloquear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> bloquear(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        socioService.bloquear(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> reativar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        socioService.reativar(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/upload-imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UploadImagemResponseDTO> uploadImagem(
            @RequestParam("file") MultipartFile file,
            @RequestParam("clubeId") Long clubeId
    ) {
        return ResponseEntity.ok(
                socioImagemService.uploadImagem(file, clubeId)
        );
    }

    @GetMapping("/imagem/signed-url")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    public ResponseEntity<SignedUrlResponseDTO> gerarSignedUrl(
            @RequestParam("path") String path
    ) {
        return ResponseEntity.ok(
                socioImagemService.gerarSignedUrl(path)
        );
    }
}