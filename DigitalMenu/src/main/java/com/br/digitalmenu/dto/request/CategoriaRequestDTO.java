package com.br.digitalmenu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaRequestDTO {
    @NotBlank(message = "O nome da categoria e obrigatorio.")
    private String nomeCategoria;
    private boolean ativo;
}
