package com.br.digitalmenu.dto.request;

import lombok.Data;

@Data
public class ConfirmacaoRequestDTO {
    private String email;
    private String codigo;
}
