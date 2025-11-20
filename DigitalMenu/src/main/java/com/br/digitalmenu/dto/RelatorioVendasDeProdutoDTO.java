package com.br.digitalmenu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RelatorioVendasDeProdutoDTO {
    String nomeProduto;
    Long qtde;
    BigDecimal total;
    Long idCategoria;

    public RelatorioVendasDeProdutoDTO(String nomeProduto, Long qtde, BigDecimal total, Long idCategoria) {
        this.nomeProduto = nomeProduto;
        this.qtde = qtde;
        this.total = total;
        this.idCategoria = idCategoria;
    }
}
