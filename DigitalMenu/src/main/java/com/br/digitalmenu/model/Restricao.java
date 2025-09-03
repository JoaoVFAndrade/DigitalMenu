package com.br.digitalmenu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Restricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_restricao")
    private Long idRestricao;

    @Column(unique = true,nullable = false, length = 50, name = "nome_restricao")
    @NotBlank
    private String nomeRestricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, name = "tipo_restricao")
    private TipoRestricao tipoRestricao;

    private Boolean ativo = true;

    @ManyToMany(mappedBy = "restricoes")
    @JsonBackReference
    private Set<Ingrediente> ingredientes = new HashSet<>();
}
