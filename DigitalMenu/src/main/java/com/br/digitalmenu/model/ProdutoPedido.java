package com.br.digitalmenu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@ToString(exclude = "pedido")
public class ProdutoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonIgnore
    private Pedido pedido;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate data = LocalDate.now();

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subTotal;

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalTime horario = LocalTime.now();

    @Enumerated(EnumType.STRING)
    private StatusProdutoPedido status = StatusProdutoPedido.EM_PREPARACAO;

}
