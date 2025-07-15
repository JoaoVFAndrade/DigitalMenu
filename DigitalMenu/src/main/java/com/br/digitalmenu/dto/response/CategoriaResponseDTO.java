package com.br.digitalmenu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaResponseDTO {
    private Long id;
    private String nomeCategoria;
    private boolean ativo;
}
