package com.br.digitalmenu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
@Data
@Entity(name = "produto")
public class Produto {
    @Id
    @Column(name = "id_produto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduto;

    @Column(name = "nome_produto", length = 50, nullable = false)
    @NotBlank
    private String nomeProduto;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false, name = "preco")
    private BigDecimal preco;

    @Column(name = "horario_inicial")
    @JsonFormat(pattern = "HH:mm", timezone = "America/Sao_Paulo")
    private LocalTime horarioInicial;

    @Column(name = "horario_final")
    @JsonFormat(pattern = "HH:mm", timezone = "America/Sao_Paulo")
    private LocalTime horarioFinal;

    @Column(name = "url_imagem")
    private String foto;

    @Column(name = "em_estoque")
    @NotNull
    private Boolean estoque;

    @Column(name = "ativo")
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToMany
    @JoinTable(
            name = "produto_ingrediente",
            joinColumns = @JoinColumn(name = "id_produto"),
            inverseJoinColumns = @JoinColumn(name = "id_ingrediente")
    )
    private List<Ingrediente> ingrediente;

    @ManyToMany
    @JoinTable(
            name = "produto_restricao",
            joinColumns = @JoinColumn(name = "id_produto"),
            inverseJoinColumns = @JoinColumn(name = "id_restricao")
    )
    private List<Restricao> restricao;

    @ManyToMany
    @JoinTable(
            name = "produto_dia_semana",
            joinColumns = @JoinColumn(name = "id_produto"),
            inverseJoinColumns = @JoinColumn(name = "id_dia")
    )
    private List<DiaSemana> diasSemana;

}

