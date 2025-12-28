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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DependenteService {

    private final DependenteRepository dependenteRepository;
    private final SocioRepository socioRepository;
    private final DependenteMapper dependenteMapper;

    public DependenteService(DependenteRepository dependenteRepository, SocioRepository socioRepository, DependenteMapper dependenteMapper) {
        this.dependenteRepository = dependenteRepository;
        this.socioRepository = socioRepository;
        this.dependenteMapper = dependenteMapper;
    }

    public DependenteResponseDTO criar(DependenteCreateDTO dto, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        SocioModel socio = buscarSocioDoClube(dto.getSocioId(), usuarioLogado);

        DependenteModel dependente =
                dependenteMapper.toEntity(dto, socio);

        dependente.setStatus(StatusDependente.ATIVO);

        return dependenteMapper.toResponseDTO(
                dependenteRepository.save(dependente)
        );
    }

    public List<DependenteResumoDTO> listarPorSocio(Long socioId, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        SocioModel socio = buscarSocioDoClube(socioId, usuarioLogado);

        return dependenteRepository.findBySocio(socio)
                .stream()
                .map(dependenteMapper::toResumoDTO)
                .toList();
    }

    public DependenteResponseDTO buscarPorId(Long dependenteId, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        DependenteModel dependente = buscarDependenteDoClube(dependenteId, usuarioLogado);

        return dependenteMapper.toResponseDTO(dependente);
    }

    public DependenteResponseDTO atualizar(Long dependenteId, DependenteUpdateDTO dto, UsuarioModel usuarioLogado) {
        validarUsuarioComClube(usuarioLogado);

        DependenteModel dependente =
                buscarDependenteDoClube(dependenteId, usuarioLogado);

        dependenteMapper.updateEntity(dependente, dto);

        return dependenteMapper.toResponseDTO(
                dependenteRepository.save(dependente)
        );
    }

    public void inativar(Long dependenteId, UsuarioModel usuarioLogado) {
        DependenteModel dependente =
                buscarDependenteDoClube(dependenteId, usuarioLogado);

        dependente.setStatus(StatusDependente.INATIVO);
        dependenteRepository.save(dependente);
    }

    public void reativar(Long dependenteId, UsuarioModel usuarioLogado) {
        DependenteModel dependente =
                buscarDependenteDoClube(dependenteId, usuarioLogado);

        dependente.setStatus(StatusDependente.ATIVO);
        dependenteRepository.save(dependente);
    }

    private void validarUsuarioComClube(UsuarioModel usuarioLogado) {
        if (usuarioLogado.getClube() == null) {
            throw new SecurityException(
                    "Usuário não está vinculado a um clube"
            );
        }
    }

    private SocioModel buscarSocioDoClube(Long socioId, UsuarioModel usuarioLogado) {
        SocioModel socio = socioRepository.findById(socioId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Sócio não encontrado")
                );

        if (!socio.getClube().getId()
                .equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException(
                    "Sócio não pertence ao seu clube"
            );
        }

        return socio;
    }

    private DependenteModel buscarDependenteDoClube(Long dependenteId, UsuarioModel usuarioLogado) {
        DependenteModel dependente = dependenteRepository.findById(dependenteId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Dependente não encontrado")
                );

        if (!dependente.getSocio().getClube().getId()
                .equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException(
                    "Dependente não pertence ao seu clube"
            );
        }

        return dependente;
    }
}
