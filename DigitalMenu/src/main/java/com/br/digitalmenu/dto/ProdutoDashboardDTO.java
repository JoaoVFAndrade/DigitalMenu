package com.br.digitalmenu.dto;

public record ProdutoDashboardDTO(
        Long idProduto,
        String nomeProduto,
        Long idCategoria,
        String nomeCategoria,
        Long qtde,
        Double preco,
        Double total
) {
}
