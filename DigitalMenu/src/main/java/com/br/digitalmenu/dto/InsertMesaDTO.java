package com.br.digitalmenu.dto;

import com.br.digitalmenu.model.Mesa;
import com.br.digitalmenu.validacoes.UniqueValue;
import jakarta.validation.constraints.NotBlank;

public record InsertMesaDTO(
        @NotBlank
        @UniqueValue(entity = Mesa.class, fieldName = "numeroMesa")
        String numeroMesa
) {
}
