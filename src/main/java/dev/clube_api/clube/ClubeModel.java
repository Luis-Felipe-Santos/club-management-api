package dev.clube_api.clube;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="clubes")
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

    @Column(name="data_cadastro")
    private LocalDateTime dataCriacao;
}
