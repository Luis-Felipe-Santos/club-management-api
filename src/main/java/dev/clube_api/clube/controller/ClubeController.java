package dev.clube_api.clube.controller;

import dev.clube_api.clube.dto.ClubeCreateDTO;
import dev.clube_api.clube.dto.ClubeResponseDTO;
import dev.clube_api.clube.dto.ClubeUpdateDTO;
import dev.clube_api.clube.service.ClubeService;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubes")
public class ClubeController{
    private final ClubeService clubeService;
    private final UsuarioService usuarioService;

    public ClubeController(ClubeService clubeService, UsuarioService usuarioService) {
        this.clubeService = clubeService;
        this.usuarioService = usuarioService;
    }


    @PostMapping
    public ResponseEntity<ClubeResponseDTO> criar(
            @RequestBody @Valid ClubeCreateDTO dto,
            @RequestHeader("X-USER-ID") Long id
    ) {
        UsuarioModel usuarioLogado = usuarioService.buscarEntidadePorId(id);

        ClubeResponseDTO response = clubeService.criarClube(dto, usuarioLogado);

        return ResponseEntity.ok(
                response
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> buscarPorId(
            @PathVariable Long id
    ) {
        ClubeResponseDTO response = clubeService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClubeResponseDTO>> listar() {
        List<ClubeResponseDTO> response = clubeService.listar();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody ClubeUpdateDTO dto
    ) {
        ClubeResponseDTO response = clubeService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
            clubeService.deletar(id);
            return ResponseEntity.ok("Clube deletado com sucesso");
    }
}
