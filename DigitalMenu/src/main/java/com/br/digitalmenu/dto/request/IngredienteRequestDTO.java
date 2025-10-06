package com.br.digitalmenu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class IngredienteRequestDTO {
    private String nomeIngrediente;

    private Boolean estoque;

    private boolean ativo;

    private List<Long> idRestricoes;
}
