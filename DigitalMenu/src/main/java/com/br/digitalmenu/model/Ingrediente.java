package com.br.digitalmenu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "ingrediente")
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingrediente")
    private Long idIngrediente;

    @Column(name = "nome_ingrediente", length = 50, nullable = false)
    @NotBlank
    private String nomeIngrediente;

    @Column(name = "em_estoque")
    @NotNull
    private Boolean estoque;

    private Boolean ativo;

    @ManyToMany
    @JoinTable(
            name = "restricao_ingrediente",
            joinColumns = @JoinColumn(name = "id_ingrediente"),
            inverseJoinColumns = @JoinColumn(name = "id_restricao")
    )
    @JsonManagedReference
    private Set<Restricao> restricoes = new HashSet<>();
}
