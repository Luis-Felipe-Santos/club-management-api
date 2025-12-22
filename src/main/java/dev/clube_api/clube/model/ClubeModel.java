package dev.clube_api.clube.model;


import dev.clube_api.clube.enums.StatusClube;
import dev.clube_api.usuario.model.UsuarioModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="clubes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="nome", nullable = false)
    private String nome;

    @Column(name="cnpj", unique = true, length = 14, nullable = false)
    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private StatusClube status;

    @Column(name="data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @ManyToOne
    @JoinColumn(name = "usuario_admin_id", nullable = false)
    private UsuarioModel admin;

}
