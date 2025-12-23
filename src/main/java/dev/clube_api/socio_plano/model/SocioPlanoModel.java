package dev.clube_api.socio_plano.model;

import dev.clube_api.plano.model.PlanoModel;
import dev.clube_api.socio.model.SocioModel;
import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "socios_planos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SocioPlanoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "socio_id", nullable = false)
    private SocioModel socio;

    @ManyToOne
    @JoinColumn(name = "plano_id", nullable = false)
    private PlanoModel plano;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSocioPlano status;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}