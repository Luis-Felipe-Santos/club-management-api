package dev.clube_api.socio_plano.service;

import dev.clube_api.plano.model.PlanoModel;
import dev.clube_api.plano.repository.PlanoRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio.repository.SocioRepository;
import dev.clube_api.socio_plano.dto.SocioPlanoCreateDTO;
import dev.clube_api.socio_plano.dto.SocioPlanoResponseDTO;
import dev.clube_api.socio_plano.dto.SocioPlanoResumoDTO;
import dev.clube_api.socio_plano.dto.SocioPlanoUpdateDTO;
import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import dev.clube_api.socio_plano.mapper.SocioPlanoMapper;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.stereotype.Service;
import dev.clube_api.socio_plano.repository.SocioPlanoRepository;

import java.util.List;


@Service
public class SocioPlanoService {

    private final SocioPlanoRepository socioPlanoRepository;
    private final SocioPlanoMapper socioPlanoMapper;
    private final SocioRepository socioRepository;
    private final PlanoRepository planoRepository;

    public SocioPlanoService(
            SocioPlanoRepository socioPlanoRepository,
            SocioPlanoMapper socioPlanoMapper,
            SocioRepository socioRepository,
            PlanoRepository planoRepository
    ) {
        this.socioPlanoRepository = socioPlanoRepository;
        this.socioPlanoMapper = socioPlanoMapper;
        this.socioRepository = socioRepository;
        this.planoRepository = planoRepository;
    }

    public SocioPlanoResponseDTO vincular(SocioPlanoCreateDTO dto, UsuarioModel usuarioLogado) {
        SocioModel socio = socioRepository.findById(dto.getSocioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sócio não encontrado"));

        PlanoModel plano = planoRepository.findById(dto.getPlanoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado"));

        if (!socio.getClube().getId().equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Sócio não pertence ao seu clube");
        }

        if (socioPlanoRepository.existsBySocioAndPlanoAndStatus(socio, plano, StatusSocioPlano.ATIVO)) {
            throw new IllegalStateException("Sócio já está ativo neste plano");
        }

        SocioPlanoModel sp = socioPlanoMapper.toEntity(socio, plano);

        return socioPlanoMapper.toResponseDTO(socioPlanoRepository.save(sp));
    }

    public List<SocioPlanoResumoDTO> listarPorSocio(Long socioId, UsuarioModel usuarioLogado) {
        SocioModel socio = socioRepository.findById(socioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sócio não encontrado"));

        if (!socio.getClube().getId().equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Sócio não pertence ao seu clube");
        }

        return socioPlanoRepository.findBySocio(socio)
                .stream()
                .map(socioPlanoMapper::toResumoDTO)
                .toList();
    }

    public SocioPlanoResponseDTO atualizarStatus(Long socioPlanoId, SocioPlanoUpdateDTO dto, UsuarioModel usuarioLogado) {
        SocioPlanoModel sp = socioPlanoRepository.findById(socioPlanoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vínculo não encontrado"));

        if (!sp.getSocio().getClube().getId()
                .equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Vínculo não pertence ao seu clube");
        }

        socioPlanoMapper.updateEntity(sp, dto);

        return socioPlanoMapper.toResponseDTO(socioPlanoRepository.save(sp));
    }

}
