package dev.clube_api.plano.model;

import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.plano.enums.PeriodicidadePlano;
import dev.clube_api.plano.enums.StatusPlano;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="planos")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoModel {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodicidadePlano periodicidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPlano status;

    @ManyToOne
    @JoinColumn(name = "clube_id", nullable = false )
    private ClubeModel clube;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
