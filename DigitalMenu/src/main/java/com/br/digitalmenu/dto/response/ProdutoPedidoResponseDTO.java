package com.br.digitalmenu.dto.response;

import com.br.digitalmenu.model.ProdutoPedido;
import com.br.digitalmenu.model.StatusProdutoPedido;

import java.math.BigDecimal;
import java.time.LocalTime;

public record ProdutoPedidoResponseDTO(
        Long idProdutoPedido,
        Long idPedido,
        String nomeProduto,
        Long idProduto,
        StatusProdutoPedido status,
        LocalTime horarioPedido,
        String numeroMesa,
        Integer quantidade,
        BigDecimal subTotal
) {
    public ProdutoPedidoResponseDTO(ProdutoPedido produtoPedido){
        this(produtoPedido.getId(),
                produtoPedido.getPedido().getId(),
                produtoPedido.getProduto().getNomeProduto(),
                produtoPedido.getProduto().getIdProduto(),
                produtoPedido.getStatus(),
                produtoPedido.getHorario(),
                produtoPedido.getPedido().getMesa().getNumeroMesa(),
                produtoPedido.getQuantidade(),
                produtoPedido.getSubTotal());
    }
}
