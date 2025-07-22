package com.br.digitalmenu.dto;

import com.br.digitalmenu.model.Restricao;
import com.br.digitalmenu.validacoes.UniqueValue;
import jakarta.validation.constraints.NotBlank;

public record InsertRestricaoDTO(
        @NotBlank
        @UniqueValue(entity = Restricao.class, fieldName = "nomeRestricao")
        String nomeRestricao
) {
}
