package com.br.digitalmenu.dto;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioFinalDTO(
        List<RelatorioVendasDeProdutoDTO> relatorioVendasDeProdutoDTOList,
        BigDecimal totalVendas,
        Long totalQuantidade
) {
}
