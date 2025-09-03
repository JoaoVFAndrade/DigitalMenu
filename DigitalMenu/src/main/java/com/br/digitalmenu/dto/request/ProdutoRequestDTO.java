package com.br.digitalmenu.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
public class ProdutoRequestDTO {
    private String nomeProduto;
    private Double preco;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioInicial;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioFinal;
    private String foto;
    private Boolean estoque;
    private Boolean ativo;
    private Long idCategoria;
    private List<Long> ingredientesIds;
    private List<Long> restricoesIds;
    private List<Long> diasSemanaIds;

}
