package com.br.digitalmenu.dto;

import com.br.digitalmenu.model.Restricao;
import com.br.digitalmenu.model.TipoRestricao;
import com.br.digitalmenu.validacoes.EntityExists;
import jakarta.validation.constraints.NotNull;

public record RestricaoDTO(
        @NotNull
        @EntityExists(entityClass = Restricao.class)
        Long idRestricao,
        String nomeRestricao,
        TipoRestricao tipoRestricao
) {
}
