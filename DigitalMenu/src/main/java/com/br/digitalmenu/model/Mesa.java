package com.br.digitalmenu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesa;

    @Column(unique = true, length = 25, nullable = false)
    private String numeroMesa;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(nullable = false, name = "qtde_assentos")
    @Min(1)
    @Positive
    private Integer qtdeAssentos;
}
