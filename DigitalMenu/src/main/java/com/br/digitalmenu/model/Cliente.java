package com.br.digitalmenu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "cliente")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(nullable = false, length = 80)
    @NotBlank(message = "O nome e obrigatorio")
    private String nome;

    @Column(nullable = false, unique = true)
    @Email(message = "E-mail invalido")
    @NotBlank(message = "o email e obrigatorio")
    private String email;

    @Column(nullable = false, length = 128)
    @NotBlank(message = "A senha e obrigatoria")
    private String senha;

    @Column(nullable = false)
    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private boolean emailValidado = false;

    @ManyToMany
    private List<Restricao> restricoes;

    @Enumerated(EnumType.STRING)
    private RoleNome role = RoleNome.CLIENTE;
}
