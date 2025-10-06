package com.br.digitalmenu.dto;

import com.br.digitalmenu.model.Pedido;
import com.br.digitalmenu.model.Produto;
import com.br.digitalmenu.validacoes.EntityExists;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InsertProdutoPedidoDTO(
        @NotNull
        @EntityExists(entityClass = Pedido.class)
        Long idPedido,
        @NotNull
        @EntityExists(entityClass = Produto.class)
        Long idProduto,
        @NotNull
        @Min(1)
        Integer quantidade
) {
}
