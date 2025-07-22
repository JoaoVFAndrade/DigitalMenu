package com.br.digitalmenu.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Restricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRestricao;

    @Column(unique = true,nullable = false, length = 50)
    private String nomeRestricao;

    private Boolean ativo = true;
}
