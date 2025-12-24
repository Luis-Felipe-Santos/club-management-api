package dev.clube_api.socio_plano_historico.service;


import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import dev.clube_api.socio_plano_historico.enums.AcaoSocioPlano;
import dev.clube_api.socio_plano_historico.model.SocioPlanoHistoricoModel;
import dev.clube_api.socio_plano_historico.repository.SocioPlanoHistoricoRepository;
import dev.clube_api.usuario.model.UsuarioModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioPlanoHistoricoService {
    private final SocioPlanoHistoricoRepository repository;

    public SocioPlanoHistoricoService(SocioPlanoHistoricoRepository repository) {
        this.repository = repository;
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

    public List<SocioPlanoHistoricoModel>
    listarPorSocioPlano(SocioPlanoModel socioPlano) {
        return repository.findBySocioPlanoOrderByCreatedAtDesc(socioPlano);
    }
}
