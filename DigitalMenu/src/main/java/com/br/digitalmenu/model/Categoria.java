package com.br.digitalmenu.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "categoria")
public class Categoria {

    @Id
    @Column(name = "id_categoria")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @Column(name = "nome_categoria")
    private String nomeCategoria;

    private Boolean ativo;
}
