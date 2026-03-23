package dev.clube_api.clube.controller;

import dev.clube_api.clube.dto.ClubeCreateDTO;
import dev.clube_api.clube.dto.ClubeResponseDTO;
import dev.clube_api.clube.dto.ClubeUpdateDTO;
import dev.clube_api.clube.service.ClubeService;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    private final ClubeService clubeService;
    private final UsuarioService usuarioService;

    public ClubeController(ClubeService clubeService, UsuarioService usuarioService) {
        this.clubeService = clubeService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClubeResponseDTO> criar(
            @RequestBody @Valid ClubeCreateDTO dto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(email);

        return ResponseEntity.ok(
                clubeService.criarClube(dto, usuarioLogado)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    public ResponseEntity<ClubeResponseDTO> buscarPorId(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(email);

        return ResponseEntity.ok(
                clubeService.buscarPorId(id, usuarioLogado)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    public ResponseEntity<List<ClubeResponseDTO>> listar(Authentication authentication) {
        String email = authentication.getName();
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(email);

        return ResponseEntity.ok(
                clubeService.listarPorUsuario(usuarioLogado)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClubeResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody ClubeUpdateDTO dto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(email);

        ClubeResponseDTO response = clubeService.atualizar(id, dto, usuarioLogado);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> inativar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(email);

        clubeService.inativar(id, usuarioLogado);
        return ResponseEntity.ok("Clube inativado com sucesso");
    }

    @PatchMapping("/{id}/bloquear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> bloquear(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(email);

        clubeService.bloquear(id, usuarioLogado);
        return ResponseEntity.ok("Clube bloqueado com sucesso");
    }

    @PatchMapping("/{id}/reativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reativar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(email);

        clubeService.reativar(id, usuarioLogado);
        return ResponseEntity.ok("Clube reativado com sucesso");
    }
}