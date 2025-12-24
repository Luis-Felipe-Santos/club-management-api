package dev.clube_api.socio_plano_historico.model;

import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import dev.clube_api.socio_plano.model.SocioPlanoModel;
import dev.clube_api.socio_plano_historico.enums.AcaoSocioPlano;
import dev.clube_api.usuario.model.UsuarioModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "socios_planos_historico")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioPlanoHistoricoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_plano_id", nullable = false)
    private SocioPlanoModel socioPlano;


    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", nullable = false)
    private StatusSocioPlano statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false)
    private  StatusSocioPlano statusNovo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcaoSocioPlano acao;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;
}
