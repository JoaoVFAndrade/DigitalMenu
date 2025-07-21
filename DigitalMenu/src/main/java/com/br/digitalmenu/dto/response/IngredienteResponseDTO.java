package com.br.digitalmenu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredienteResponseDTO {
    private Long idIgrediente;
    private String nomeIgrediente;
    private Integer estoque;
    private boolean ativo;

}
