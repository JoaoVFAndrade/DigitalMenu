package com.br.digitalmenu.dto;

import com.br.digitalmenu.model.Restricao;
import com.br.digitalmenu.model.TipoRestricao;
import com.br.digitalmenu.validacoes.UniqueValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InsertRestricaoDTO(
        @NotBlank
        @UniqueValue(entity = Restricao.class, fieldName = "nomeRestricao")
        String nomeRestricao,

        @NotNull
        TipoRestricao tipoRestricao
) {
}
