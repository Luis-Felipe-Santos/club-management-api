package dev.clube_api.clube.controller;

import dev.clube_api.clube.dto.ClubeCreateDTO;
import dev.clube_api.clube.dto.ClubeResponseDTO;
import dev.clube_api.clube.dto.ClubeUpdateDTO;
import dev.clube_api.clube.service.ClubeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubes")
public class ClubeController{
    private final ClubeService clubeService;

    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @PostMapping("/criar")
    public ResponseEntity<ClubeResponseDTO> criar(
            @RequestBody @Valid ClubeCreateDTO dto
    ) {
        ClubeResponseDTO response = clubeService.criarClube(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<ClubeResponseDTO> buscarPorId(
            @PathVariable Long id
    ) {
        ClubeResponseDTO response = clubeService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ClubeResponseDTO>> listar() {
        List<ClubeResponseDTO> response = clubeService.listar();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ClubeResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody ClubeUpdateDTO dto
    ) {
        ClubeResponseDTO response = clubeService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }




}
