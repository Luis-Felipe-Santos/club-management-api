package dev.clube_api.socio_plano_historico.service;


import dev.clube_api.shared.exception.RecursoNaoEncontradoException;
import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import dev.clube_api.socio_plano.repository.SocioPlanoRepository;
import dev.clube_api.socio_plano_historico.enums.AcaoSocioPlano;
import dev.clube_api.socio_plano_historico.model.SocioPlanoHistoricoModel;
import dev.clube_api.socio_plano_historico.repository.SocioPlanoHistoricoRepository;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioPlanoHistoricoService {

    private final SocioPlanoHistoricoRepository repository;
    private final SocioPlanoRepository socioPlanoRepository;

    public SocioPlanoHistoricoService(
            SocioPlanoHistoricoRepository repository,
            SocioPlanoRepository socioPlanoRepository
    ) {
        this.repository = repository;
        this.socioPlanoRepository = socioPlanoRepository;
    }

    public void registrar(
            SocioPlanoModel socioPlano,
            StatusSocioPlano statusAnterior,
            StatusSocioPlano statusNovo,
            AcaoSocioPlano acao,
            UsuarioModel usuario
    ) {
        SocioPlanoHistoricoModel historico = new SocioPlanoHistoricoModel();

        historico.setSocioPlano(socioPlano);
        historico.setStatusAnterior(statusAnterior);
        historico.setStatusNovo(statusNovo);
        historico.setAcao(acao);
        historico.setUsuario(usuario);

        repository.save(historico);
    }

    public List<SocioPlanoHistoricoModel> listarPorSocioPlano(
            Long socioPlanoId,
            UsuarioModel usuarioLogado
    ) {
        SocioPlanoModel socioPlano = socioPlanoRepository.findById(socioPlanoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Vínculo não encontrado")
                );

        if (!socioPlano.getSocio().getClube().getId()
                .equals(usuarioLogado.getClube().getId())) {
            throw new SecurityException("Acesso negado");
        }

        return repository.findBySocioPlanoOrderByCreatedAtDesc(socioPlano);
    }
}
