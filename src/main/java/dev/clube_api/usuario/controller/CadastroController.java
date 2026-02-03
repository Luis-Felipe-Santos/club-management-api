package dev.clube_api.usuario.controller;

import dev.clube_api.usuario.dto.CadastroRequestDTO;
import dev.clube_api.usuario.dto.UsuarioResponseDTO;
import dev.clube_api.usuario.service.CadastroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cadastro")
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(
            @RequestBody @Valid CadastroRequestDTO dto
    ) {
        return ResponseEntity.ok(
                cadastroService.cadastrar(dto)
        );
    }
}
