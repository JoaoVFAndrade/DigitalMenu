package com.br.digitalmenu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

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
    private Double preco;
    private Integer estoque;
    private LocalTime horarioInicial;
    private LocalTime horarioFinal;
    private String foto;
    private Boolean ativo;
    private String nomeCategoria;
    private List<String> ingredientes;
    private List<String> restricoes;
    private List<String> diasSemana;
}
