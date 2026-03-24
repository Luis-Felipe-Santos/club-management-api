package dev.clube_api.socio.model;

import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.socio.enums.StatusSocio;
import dev.clube_api.socio.enums.TipoDocumentoSocio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="socios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SocioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private TipoDocumentoSocio tipoDocumento;

    @Column(nullable = true, length = 14)
    private String documento;

    @Column(nullable = true)
    private String telefone;

    @Column
    private String email;

    @Column
    private String endereco;

    @Column
    private String imagemUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSocio status;

    @ManyToOne
    @JoinColumn(name = "clube_id", nullable = false)
    private ClubeModel clube;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
