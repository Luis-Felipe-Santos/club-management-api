package dev.clube_api.socio_plano.controller;


import dev.clube_api.socio_plano.dto.SocioPlanoCreateDTO;
import dev.clube_api.socio_plano.dto.SocioPlanoResponseDTO;
import dev.clube_api.socio_plano.dto.SocioPlanoResumoDTO;
import dev.clube_api.socio_plano.service.SocioPlanoService;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socio-planos")
public class SocioPlanoController {

    private final SocioPlanoService socioPlanoService;
    private final UsuarioService usuarioService;

    public SocioPlanoController(SocioPlanoService socioPlanoService, UsuarioService usuarioService ) {
        this.socioPlanoService = socioPlanoService;
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SocioPlanoResponseDTO> vincular(
            @RequestBody @Valid SocioPlanoCreateDTO dto,
            Authentication authentication
            ){
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(socioPlanoService.vincular(dto, usuarioLogado));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping("/socio/{id}")
    public ResponseEntity<List<SocioPlanoResumoDTO>> listarPorSocio(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        return  ResponseEntity.ok(socioPlanoService.listarPorSocio(id, usuarioLogado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/suspender")
    public ResponseEntity<SocioPlanoResponseDTO> suspender(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(socioPlanoService.suspender(id, usuarioLogado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<SocioPlanoResponseDTO> cancelar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(socioPlanoService.cancelar(id, usuarioLogado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<SocioPlanoResponseDTO> reativar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(socioPlanoService.reativar(id, usuarioLogado));
    }

}
