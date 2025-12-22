package dev.clube_api.usuario.controller;

import dev.clube_api.usuario.dto.*;
import dev.clube_api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            @RequestBody @Valid UsuarioCreateDTO dto
    )
    {
        UsuarioResponseDTO response = usuarioService.criar(dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorID( @PathVariable Long id){
        UsuarioResponseDTO response = usuarioService.buscarPorID(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar(){
        List<UsuarioResponseDTO> response = usuarioService.listar();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioUpdateDTO dto){
        UsuarioResponseDTO response = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(response);
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
