package dev.clube_api.usuario.model;


import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.usuario.enums.RoleUsuario;
import dev.clube_api.usuario.enums.StatusUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModel {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imagem_url")
    private String imagemUrl;

    @Column(name="nome", nullable = false)
    private String nome;

    @Column(name="cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="senha", nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "permissao")
    private RoleUsuario role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusUsuario status;


    @ManyToOne
    @JoinColumn(name="clube_id", nullable = false)
    private ClubeModel clube;

}
