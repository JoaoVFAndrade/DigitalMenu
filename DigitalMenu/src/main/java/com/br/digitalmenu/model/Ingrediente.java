package com.br.digitalmenu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity(name = "ingrediente")
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingrediente")
    private Long idIngrediente;

    @Column(name = "nome_ingrediente", length = 50)
    @NotBlank
    private String nomeIngrediente;

    @Column(name = "em_estoque")
    @NotNull
    private Integer estoque;

    private Boolean ativo;
}
