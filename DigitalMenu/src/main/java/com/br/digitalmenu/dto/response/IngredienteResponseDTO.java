package com.br.digitalmenu.dto.response;

import com.br.digitalmenu.model.Ingrediente;
import com.br.digitalmenu.model.Restricao;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IngredienteResponseDTO {
    private Long idIngrediente;
    private String nomeIngrediente;
    private Boolean estoque;
    private boolean ativo;
    private List<String> restricoes;

    public IngredienteResponseDTO(Ingrediente ingrediente) {
        this.idIngrediente = ingrediente.getIdIngrediente();
        this.nomeIngrediente = ingrediente.getNomeIngrediente();
        this.estoque = ingrediente.getEstoque();
        this.ativo = ingrediente.getAtivo();
        this.restricoes = ingrediente.getRestricoes().stream().map(Restricao::getNomeRestricao).toList();
    }
}
