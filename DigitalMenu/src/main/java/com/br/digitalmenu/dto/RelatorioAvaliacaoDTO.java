package com.br.digitalmenu.dto;

import com.br.digitalmenu.dto.response.AvaliacaoReponseDTO;

import java.util.List;

public record RelatorioAvaliacaoDTO(
        Integer quantidadeAvaliacao,
        Double mediaNotaAtendimento,
        Double mediaNotaQualidadeDosProdutos,
        Double mediaNotaAmbiente,
        List<AvaliacaoReponseDTO> avaliacaoReponseDTO
) {
}
