package com.br.digitalmenu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfirmacaoResponseDTO {
    private String token;
    private String nome;
}
