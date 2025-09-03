package com.br.digitalmenu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IngredienteRequestDTO {
    private String nomeIngrediente;

    private Boolean estoque;

    private boolean ativo;
}
