package com.br.digitalmenu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Restricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_restricao")
    private Long idRestricao;

    @Column(unique = true,nullable = false, length = 50)
    @NotBlank
    private String nomeRestricao;

    private Boolean ativo = true;
}
