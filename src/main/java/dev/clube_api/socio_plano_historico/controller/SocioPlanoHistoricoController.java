package dev.clube_api.socio_plano_historico.controller;


import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import dev.clube_api.socio_plano.repository.SocioPlanoRepository;
import dev.clube_api.socio_plano_historico.dto.SocioPlanoHistoricoResponseDTO;
import dev.clube_api.socio_plano_historico.mapper.SocioPlanoHistoricoMapper;
import dev.clube_api.socio_plano_historico.service.SocioPlanoHistoricoService;
import dev.clube_api.usuario.model.UsuarioModel;
import dev.clube_api.usuario.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/socio-planos")
public class SocioPlanoHistoricoController {

    private final SocioPlanoHistoricoService historicoService;
    private final SocioPlanoHistoricoMapper historicoMapper;
    private final SocioPlanoRepository socioPlanoRepository;
    private final UsuarioService usuarioService;

    public SocioPlanoHistoricoController(SocioPlanoHistoricoService historicoService, SocioPlanoHistoricoMapper historicoMapper, SocioPlanoRepository socioPlanoRepository, UsuarioService usuarioService){
        this.historicoService = historicoService;
        this.historicoMapper = historicoMapper;
        this.socioPlanoRepository = socioPlanoRepository;
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO'")
    @GetMapping("/{id}/historico")
    public ResponseEntity<List<SocioPlanoHistoricoResponseDTO>> listarHistorico(
            @PathVariable Long id,
            Authentication authentication
    )
    {
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        SocioPlanoModel socioPlano = socioPlanoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vinculo não encontrado"));

        if(!socioPlano.getSocio().getClube().getId().equals(usuarioLogado.getClube().getId())){
            throw new SecurityException("Acesso negado");
        }
        return ResponseEntity.ok(historicoService.listarPorSocioPlano(socioPlano)
                .stream()
                .map(historicoMapper::toDTO)
                .toList()
        );
    }


}
