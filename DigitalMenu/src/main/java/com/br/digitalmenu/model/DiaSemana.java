package com.br.digitalmenu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class DiaSemana {

    @Id
    @Column(name = "id_dia")
    private Long idDia;

    @Column(name = "nome_dia", length = 13)
    private String nomeDia;

}
