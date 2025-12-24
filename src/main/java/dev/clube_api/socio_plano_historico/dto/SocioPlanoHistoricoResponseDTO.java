package dev.clube_api.socio_plano_historico.dto;

import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import dev.clube_api.socio_plano_historico.enums.AcaoSocioPlano;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioPlanoHistoricoResponseDTO {

    private AcaoSocioPlano acao;
    private StatusSocioPlano statusAnterior;
    private StatusSocioPlano statusNovo;
    private LocalDateTime data;
}
