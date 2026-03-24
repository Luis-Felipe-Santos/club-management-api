package dev.clube_api.socio.service;

import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.clube.repository.ClubeRepository;
import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.socio.dto.SocioCreateDTO;
import dev.clube_api.socio.dto.SocioResponseDTO;
import dev.clube_api.socio.dto.SocioResumoDTO;
import dev.clube_api.socio.dto.SocioUpdateDTO;
import dev.clube_api.socio.enums.StatusSocio;
import dev.clube_api.socio.enums.TipoDocumentoSocio;
import dev.clube_api.socio.mapper.SocioMapper;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio.repository.SocioRepository;
import dev.clube_api.socio_plano.dto.SocioPlanoResumoDTO;
import dev.clube_api.socio_plano.service.SocioPlanoService;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioService {

    private final SocioRepository socioRepository;
    private final SocioMapper socioMapper;
    private final SocioPlanoService socioPlanoService;
    private final ClubeRepository clubeRepository;

    public SocioService(
            SocioRepository socioRepository,
            SocioMapper socioMapper,
            SocioPlanoService socioPlanoService,
            ClubeRepository clubeRepository
    ) {
        this.socioRepository = socioRepository;
        this.socioMapper = socioMapper;
        this.socioPlanoService = socioPlanoService;
        this.clubeRepository = clubeRepository;
    }

    public SocioResponseDTO criar(SocioCreateDTO dto, UsuarioModel usuarioLogado) {
        normalizarCampos(dto);
        validarDocumento(dto.getTipoDocumento(), dto.getDocumento());

        ClubeModel clube = buscarClubeDoUsuario(dto.getClubeId(), usuarioLogado);

        SocioModel socio = socioMapper.toEntity(dto, clube);
        socio.setStatus(StatusSocio.ATIVO);

        return socioMapper.toResponseDTO(
                socioRepository.save(socio),
                List.of()
        );
    }

    public List<SocioResumoDTO> listarPorClube(Long clubeId, UsuarioModel usuarioLogado) {
        ClubeModel clube = buscarClubeDoUsuario(clubeId, usuarioLogado);

        return socioRepository.findByClube(clube)
                .stream()
                .map(socioMapper::toResumoDTO)
                .toList();
    }

    public SocioResponseDTO buscarPorId(Long socioId, UsuarioModel usuarioLogado) {
        SocioModel socio = buscarSocioDoUsuario(socioId, usuarioLogado);

        List<SocioPlanoResumoDTO> planos =
                socioPlanoService.listarPorSocioResumo(socio, usuarioLogado);

        return socioMapper.toResponseDTO(socio, planos);
    }

    public SocioResponseDTO atualizar(Long socioId, SocioUpdateDTO dto, UsuarioModel usuarioLogado) {
        normalizarCampos(dto);
        validarDocumento(dto.getTipoDocumento(), dto.getDocumento());

        SocioModel socio = buscarSocioDoUsuario(socioId, usuarioLogado);

        socioMapper.updateEntity(socio, dto);

        var planos = socioPlanoService.listarPorSocioResumo(socio, usuarioLogado);

        return socioMapper.toResponseDTO(
                socioRepository.save(socio),
                planos
        );
    }

    public void inativar(Long socioId, UsuarioModel usuarioLogado) {
        SocioModel socio = buscarSocioDoUsuario(socioId, usuarioLogado);

        socio.setStatus(StatusSocio.INATIVO);
        socioRepository.save(socio);
    }

    public void bloquear(Long socioId, UsuarioModel usuarioLogado) {
        SocioModel socio = buscarSocioDoUsuario(socioId, usuarioLogado);

        socio.setStatus(StatusSocio.BLOQUEADO);
        socioRepository.save(socio);
    }

    public void reativar(Long socioId, UsuarioModel usuarioLogado) {
        SocioModel socio = buscarSocioDoUsuario(socioId, usuarioLogado);

        socio.setStatus(StatusSocio.ATIVO);
        socioRepository.save(socio);
    }

    private void validarDocumento(TipoDocumentoSocio tipoDocumento, String documento) {
        boolean temTipoDocumento = tipoDocumento != null;
        boolean temDocumento = documento != null && !documento.isBlank();

        if (temTipoDocumento && !temDocumento) {
            throw new IllegalArgumentException(
                    "Documento deve ser informado quando o tipo de documento for preenchido."
            );
        }

        if (!temTipoDocumento && temDocumento) {
            throw new IllegalArgumentException(
                    "Tipo de documento deve ser informado quando o documento for preenchido."
            );
        }

        if (temDocumento && (documento.length() < 11 || documento.length() > 14)) {
            throw new IllegalArgumentException(
                    "Documento deve ter entre 11 e 14 caracteres."
            );
        }
    }

    private void normalizarCampos(SocioCreateDTO dto) {
        dto.setDocumento(normalizarTexto(dto.getDocumento()));
        dto.setTelefone(normalizarTexto(dto.getTelefone()));
        dto.setEmail(normalizarTexto(dto.getEmail()));
        dto.setEndereco(normalizarTexto(dto.getEndereco()));
        dto.setImagemUrl(normalizarTexto(dto.getImagemUrl()));
    }

    private void normalizarCampos(SocioUpdateDTO dto) {
        dto.setDocumento(normalizarTexto(dto.getDocumento()));
        dto.setTelefone(normalizarTexto(dto.getTelefone()));
        dto.setEmail(normalizarTexto(dto.getEmail()));
        dto.setEndereco(normalizarTexto(dto.getEndereco()));
        dto.setImagemUrl(normalizarTexto(dto.getImagemUrl()));
    }

    private String normalizarTexto(String valor) {
        if (valor == null) return null;

        String texto = valor.trim();
        return texto.isEmpty() ? null : texto;
    }

    private ClubeModel buscarClubeDoUsuario(Long clubeId, UsuarioModel usuarioLogado) {
        ClubeModel clube = clubeRepository.findById(clubeId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Clube não encontrado")
                );

        if (!clube.getAdmin().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este clube");
        }

        return clube;
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
}