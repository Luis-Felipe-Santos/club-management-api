package dev.clube_api.socio.controller;

import dev.clube_api.socio.dto.SocioCreateDTO;
import dev.clube_api.socio.dto.SocioResponseDTO;
import dev.clube_api.socio.dto.SocioResumoDTO;
import dev.clube_api.socio.dto.SocioUpdateDTO;
import dev.clube_api.socio.service.SocioService;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socios")
public class SocioController {

    private final SocioService socioService;
    private final UsuarioService usuarioService;

    public SocioController(
            SocioService socioService,
            UsuarioService usuarioService
    ) {
        this.socioService = socioService;
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
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                socioService.listarPorClube(usuarioLogado)
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
}
