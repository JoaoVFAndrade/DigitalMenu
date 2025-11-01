package com.br.digitalmenu.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvaliacao;

    @Column(nullable = false)
    private Byte notaAtendimento;

    @Column(nullable = false)
    private Byte notaAmbiente;

    @Column(nullable = false)
    private Byte notaQualidadeDosProduto;

    @Column(length = 50)
    private String comentario;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

}
