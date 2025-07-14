package com.br.digitalmenu.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "categoria")
public class Category {

    @Id
    @Column(name = "id_categoria")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategory;

    private String nameCategory;

    private Boolean active;
}
