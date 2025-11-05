package com.br.digitalmenu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponseDTO {
    private Long idProduto;
    private String nomeProduto;
    private BigDecimal preco;
    private LocalTime horarioInicial;
    private LocalTime horarioFinal;
    private String descricao;
    private String foto;
    private Boolean estoque;
    private Boolean ativo;
    private String nomeCategoria;
    private List<IngredienteResponseDTO> ingredientes;
    private List<String> restricoesProduto;
    private List<String> restricoesCombinadas;
    private List<String> diasSemana;
}
