package dev.clube_api.assinatura;


import jakarta.persistence.*;

@Entity
@Table(name="tb_assinaturas")
public class AssinaturaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="nome")
    private String nome;




}
