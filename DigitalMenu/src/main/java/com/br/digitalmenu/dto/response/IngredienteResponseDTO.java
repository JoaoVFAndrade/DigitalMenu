package com.br.digitalmenu.dto.response;

import com.br.digitalmenu.model.Ingrediente;
import com.br.digitalmenu.model.Restricao;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IngredienteResponseDTO {
    private Long idIgrediente;
    private String nomeIgrediente;
    private Boolean estoque;
    private boolean ativo;
    private List<String> restricoes;

    public IngredienteResponseDTO(Ingrediente ingrediente) {
        this.idIgrediente = ingrediente.getIdIngrediente();
        this.nomeIgrediente = ingrediente.getNomeIngrediente();
        this.estoque = ingrediente.getEstoque();
        this.ativo = ingrediente.getAtivo();
        this.restricoes = ingrediente.getRestricoes().stream().map(Restricao::getNomeRestricao).toList();
    }
}
