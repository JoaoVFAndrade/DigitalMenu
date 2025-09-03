package com.br.digitalmenu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredienteResponseDTO {
    private Long idIgrediente;
    private String nomeIgrediente;
    private Boolean estoque;
    private boolean ativo;

}
