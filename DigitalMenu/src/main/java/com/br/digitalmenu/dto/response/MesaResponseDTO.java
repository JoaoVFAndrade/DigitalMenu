package com.br.digitalmenu.dto.response;

import com.br.digitalmenu.model.Mesa;
import com.br.digitalmenu.validacoes.EntityExists;
import jakarta.validation.constraints.NotNull;

public record MesaResponseDTO(
        @NotNull
        @EntityExists(entityClass = Mesa.class)
        Long id,
        String numeroMesa,
        Boolean ativo
) {
}
