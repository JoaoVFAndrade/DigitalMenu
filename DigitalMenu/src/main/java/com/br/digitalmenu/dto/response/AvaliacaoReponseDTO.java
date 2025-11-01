package com.br.digitalmenu.dto.response;


import com.br.digitalmenu.model.Avaliacao;

public record AvaliacaoReponseDTO(
        Long idAvaliacao,
        Byte notaAtendimento,

        Byte notaAmbiente,

        Byte notaQualidadeDosProdutos,
        String comentario

) {
    public AvaliacaoReponseDTO(Avaliacao avaliacao) {
        this(avaliacao.getIdAvaliacao(), avaliacao.getNotaAtendimento(),avaliacao.getNotaAmbiente(),avaliacao.getNotaQualidadeDosProduto(),avaliacao.getComentario());

    }
}
