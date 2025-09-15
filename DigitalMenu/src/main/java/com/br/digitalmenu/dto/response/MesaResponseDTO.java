package com.br.digitalmenu.dto.response;

import com.br.digitalmenu.model.Mesa;
import com.br.digitalmenu.validacoes.EntityExists;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MesaResponseDTO(
        @NotNull
        @EntityExists(entityClass = Mesa.class)
        Long idMesa,
        String numeroMesa,
        Boolean ativo,
        @Min(1)
        Integer qtdeAssentos
) {
        public MesaResponseDTO(Mesa mesa) {
                this(mesa.getIdMesa(), mesa.getNumeroMesa(), mesa.getAtivo(), mesa.getQtdeAssentos());
        }
}
