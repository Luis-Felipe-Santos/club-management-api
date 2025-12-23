package dev.clube_api.plano.controller;

import dev.clube_api.plano.dto.PlanoCreateDTO;
import dev.clube_api.plano.dto.PlanoResponseDTO;
import dev.clube_api.plano.dto.PlanoUpdateDTO;
import dev.clube_api.plano.service.PlanoService;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanoController {

    private final PlanoService planoService;
    private final UsuarioService usuarioService;

    public PlanoController(
            PlanoService planoService,
            UsuarioService usuarioService
    ) {
        this.planoService = planoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanoResponseDTO> criar(
            @RequestBody @Valid PlanoCreateDTO dto,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado = getUsuarioLogado(authentication);

        return ResponseEntity.ok(
                planoService.criar(dto, usuarioLogado)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    public ResponseEntity<List<PlanoResponseDTO>> listar(
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado = getUsuarioLogado(authentication);

        return ResponseEntity.ok(
                planoService.listarPorClube(usuarioLogado)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid PlanoUpdateDTO dto,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado = getUsuarioLogado(authentication);

        return ResponseEntity.ok(
                planoService.atualizar(id, dto, usuarioLogado)
        );
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> inativar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado = getUsuarioLogado(authentication);

        planoService.inativar(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> reativar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado = getUsuarioLogado(authentication);

        planoService.reativar(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }


    private UsuarioModel getUsuarioLogado(Authentication authentication) {
        String email = authentication.getName();
        return usuarioService.buscarPorEmail(email);
    }
}

