package com.br.digitalmenu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionario")
    private Long idFuncionario;

    @Column(nullable = false, length = 80)
    @NotBlank(message = "O nome e obrigatorio")
    private String nome;

    @Column(nullable = false, unique = true)
    @Email(message = "E-mail invalido")
    @NotBlank(message = "O email e obrigatorio")
    private String email;

    @Column(nullable = false, length = 128)
    @NotBlank(message = "A senha e obrigatoria")
    private String senha;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "funcionario_roles", joinColumns = @JoinColumn(name = "id_funcionario"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<RoleNome> roles = new HashSet<>();


    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<RoleNome> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleNome> roles) {
        this.roles = roles;
    }
}