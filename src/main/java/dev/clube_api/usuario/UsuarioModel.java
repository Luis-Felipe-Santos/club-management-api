package dev.clube_api.usuario;


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

    @Column(name="nome", nullable = false)
    private String nome;

    @Column(name="cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="senha", nullable = false)
    private String senha;




}
