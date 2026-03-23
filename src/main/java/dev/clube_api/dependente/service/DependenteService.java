package dev.clube_api.dependente.service;

import dev.clube_api.dependente.dto.DependenteCreateDTO;
import dev.clube_api.dependente.dto.DependenteResponseDTO;
import dev.clube_api.dependente.dto.DependenteResumoDTO;
import dev.clube_api.dependente.dto.DependenteUpdateDTO;
import dev.clube_api.dependente.enums.StatusDependente;
import dev.clube_api.dependente.mapper.DependenteMapper;
import dev.clube_api.dependente.model.DependenteModel;
import dev.clube_api.dependente.repository.DependenteRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio.repository.SocioRepository;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DependenteService {

    private final DependenteRepository dependenteRepository;
    private final SocioRepository socioRepository;
    private final DependenteMapper dependenteMapper;

    public DependenteService(
            DependenteRepository dependenteRepository,
            SocioRepository socioRepository,
            DependenteMapper dependenteMapper
    ) {
        this.dependenteRepository = dependenteRepository;
        this.socioRepository = socioRepository;
        this.dependenteMapper = dependenteMapper;
    }

    public DependenteResponseDTO criar(DependenteCreateDTO dto, UsuarioModel usuarioLogado) {
        SocioModel socio = buscarSocioDoUsuario(dto.getSocioId(), usuarioLogado);

        DependenteModel dependente = dependenteMapper.toEntity(dto, socio);
        dependente.setStatus(StatusDependente.ATIVO);

        return dependenteMapper.toResponseDTO(
                dependenteRepository.save(dependente)
        );
    }

    public List<DependenteResumoDTO> listarPorSocio(Long socioId, UsuarioModel usuarioLogado) {
        SocioModel socio = buscarSocioDoUsuario(socioId, usuarioLogado);

        return dependenteRepository.findBySocio(socio)
                .stream()
                .map(dependenteMapper::toResumoDTO)
                .toList();
    }

    public DependenteResponseDTO buscarPorId(Long dependenteId, UsuarioModel usuarioLogado) {
        DependenteModel dependente = buscarDependenteDoUsuario(dependenteId, usuarioLogado);

        return dependenteMapper.toResponseDTO(dependente);
    }

    public DependenteResponseDTO atualizar(Long dependenteId, DependenteUpdateDTO dto, UsuarioModel usuarioLogado) {
        DependenteModel dependente = buscarDependenteDoUsuario(dependenteId, usuarioLogado);

        dependenteMapper.updateEntity(dependente, dto);

        return dependenteMapper.toResponseDTO(
                dependenteRepository.save(dependente)
        );
    }

    public void inativar(Long dependenteId, UsuarioModel usuarioLogado) {
        DependenteModel dependente = buscarDependenteDoUsuario(dependenteId, usuarioLogado);

        dependente.setStatus(StatusDependente.INATIVO);
        dependenteRepository.save(dependente);
    }

    public void reativar(Long dependenteId, UsuarioModel usuarioLogado) {
        DependenteModel dependente = buscarDependenteDoUsuario(dependenteId, usuarioLogado);

        dependente.setStatus(StatusDependente.ATIVO);
        dependenteRepository.save(dependente);
    }

    private SocioModel buscarSocioDoUsuario(Long socioId, UsuarioModel usuarioLogado) {
        SocioModel socio = socioRepository.findById(socioId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Sócio não encontrado")
                );

        if (!socio.getClube().getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este sócio");
        }

        return socio;
    }

    private DependenteModel buscarDependenteDoUsuario(Long dependenteId, UsuarioModel usuarioLogado) {
        DependenteModel dependente = dependenteRepository.findById(dependenteId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Dependente não encontrado")
                );

        if (!dependente.getSocio().getClube().getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este dependente");
        }

        return dependente;
    }
}