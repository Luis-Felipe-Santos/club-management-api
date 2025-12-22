package dev.clube_api.usuario.controller;

import dev.clube_api.usuario.dto.*;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(
            @RequestBody @Valid UsuarioCreateDTO dto,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                usuarioService.criar(dto, usuarioLogado)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorID(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                usuarioService.buscarPorId(id, usuarioLogado)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar(
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                usuarioService.listarPorClube(usuarioLogado)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                usuarioService.atualizar(id, dto)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<String> inativar(@PathVariable Long id) {
        usuarioService.inativar(id);
        return ResponseEntity.ok("Usuário inativado com sucesso");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/bloquear")
    public ResponseEntity<String> bloquear(@PathVariable Long id) {
        usuarioService.bloquear(id);
        return ResponseEntity.ok("Usuário bloqueado com sucesso");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/desbloquear")
    public ResponseEntity<String> desbloquear(@PathVariable Long id) {
        usuarioService.desbloquear(id);
        return ResponseEntity.ok("Usuário desbloqueado com sucesso");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/senha")
    public ResponseEntity<String> atualizarSenha(
            @PathVariable Long id,
            @RequestBody UsuarioSenhaUpdateDTO dto
    ) {
        usuarioService.atualizarSenha(id, dto);
        return ResponseEntity.ok("Senha alterada com sucesso");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/permissoes")
    public ResponseEntity<String> atualizarPermissoes(
            @PathVariable Long id,
            @RequestBody UsuarioAdminUpdateDTO dto
    ) {
        usuarioService.atualizarPermissoes(id, dto);
        return ResponseEntity.ok("Permissão alterada com sucesso");
    }
}
