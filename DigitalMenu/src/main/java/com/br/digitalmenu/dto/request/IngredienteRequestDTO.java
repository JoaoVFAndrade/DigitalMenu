package com.br.digitalmenu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IngredienteRequestDTO {
    private String nomeIngrediente;

    private Integer estoque;

    private boolean ativo;
}
