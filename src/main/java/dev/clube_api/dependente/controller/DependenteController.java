package dev.clube_api.dependente.controller;

import dev.clube_api.dependente.dto.DependenteCreateDTO;
import dev.clube_api.dependente.dto.DependenteResponseDTO;
import dev.clube_api.dependente.dto.DependenteResumoDTO;
import dev.clube_api.dependente.dto.DependenteUpdateDTO;
import dev.clube_api.dependente.imagem.service.DependenteImagemService;
import dev.clube_api.dependente.service.DependenteService;
import dev.clube_api.shared.storage.dto.SignedUrlResponseDTO;
import dev.clube_api.shared.storage.dto.UploadImagemResponseDTO;
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
@RequestMapping("/dependentes")
public class DependenteController {

    private final DependenteService dependenteService;
    private final DependenteImagemService dependenteImagemService;
    private final UsuarioService usuarioService;

    public DependenteController(
            DependenteService dependenteService,
            DependenteImagemService dependenteImagemService,
            UsuarioService usuarioService
    ) {
        this.dependenteService = dependenteService;
        this.dependenteImagemService = dependenteImagemService;
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DependenteResponseDTO> criar(
            @RequestBody @Valid DependenteCreateDTO dto,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                dependenteService.criar(dto, usuarioLogado)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<DependenteResumoDTO>> listarPorSocio(
            @PathVariable Long socioId,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                dependenteService.listarPorSocio(socioId, usuarioLogado)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<DependenteResponseDTO> buscarPorId(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                dependenteService.buscarPorId(id, usuarioLogado)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DependenteResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DependenteUpdateDTO dto,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                dependenteService.atualizar(id, dto, usuarioLogado)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        dependenteService.inativar(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        dependenteService.reativar(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/upload-imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadImagemResponseDTO> uploadImagem(
            @RequestParam("file") MultipartFile file,
            @RequestParam("socioId") Long socioId
    ) {
        return ResponseEntity.ok(
                dependenteImagemService.uploadImagem(file, socioId)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping("/imagem/signed-url")
    public ResponseEntity<SignedUrlResponseDTO> gerarSignedUrl(
            @RequestParam("path") String path
    ) {
        return ResponseEntity.ok(
                dependenteImagemService.gerarSignedUrl(path)
        );
    }
}