package com.br.digitalmenu.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "ingrediente")
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingrediente")
    private Long idIngrediente;

    @Column(name = "nome_ingrediente")
    private String nomeIngrediente;

    @Column(name = "em_estoque")
    private Integer estoque;

    private Boolean ativo;
}
