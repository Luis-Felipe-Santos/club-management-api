package dev.clube_api.pagamento.controller;

import dev.clube_api.pagamento.dto.*;
import dev.clube_api.pagamento.enums.StatusPagamento;
import dev.clube_api.pagamento.service.PagamentoService;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final UsuarioService usuarioService;

    public PagamentoController(PagamentoService pagamentoService, UsuarioService usuarioService) {
        this.pagamentoService = pagamentoService;
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping
    public ResponseEntity<List<PagamentoListaDTO>> listar(
            @RequestParam Long clubeId,
            @RequestParam(required = false) Long planoId,
            @RequestParam(required = false) YearMonth competencia,
            @RequestParam(required = false) StatusPagamento status,
            @RequestParam(required = false) String busca,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                pagamentoService.listar(
                        clubeId,
                        planoId,
                        competencia,
                        status,
                        busca,
                        usuarioLogado
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> criar(
            @RequestBody @Valid PagamentoCreateDTO dto,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                pagamentoService.criar(dto, usuarioLogado)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @PatchMapping("/{id}/quitar")
    public ResponseEntity<PagamentoResponseDTO> quitar(
            @PathVariable Long id,
            @RequestBody @Valid PagamentoQuitarDTO dto,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                pagamentoService.quitar(id, dto, usuarioLogado)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/ajustar")
    public ResponseEntity<PagamentoResponseDTO> ajustar(
            @PathVariable Long id,
            @RequestBody @Valid PagamentoAjusteDTO dto,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                pagamentoService.ajustar(id, dto, usuarioLogado)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<PagamentoResumoDTO>> listarPorSocio(
            @PathVariable Long socioId,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                pagamentoService.listarPorSocio(socioId, usuarioLogado)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping("/relatorios/inadimplentes")
    public ResponseEntity<List<InadimplenteDTO>> inadimplentes(
            @RequestParam YearMonth competencia,
            @RequestParam Long clubeId,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                pagamentoService.gerarRelatorioInadimplentes(
                        competencia,
                        clubeId,
                        usuarioLogado
                )
        );
    }
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @GetMapping("/grade")
    public ResponseEntity<List<PagamentoGradeLinhaDTO>> listarGrade(
            @RequestParam Long clubeId,
            @RequestParam Integer ano,
            @RequestParam(required = false) Long planoId,
            Authentication authentication
    ) {
        UsuarioModel usuarioLogado =
                usuarioService.buscarPorEmail(authentication.getName());

        return ResponseEntity.ok(
                pagamentoService.listarGrade(clubeId, ano, planoId, usuarioLogado)
        );
    }
}