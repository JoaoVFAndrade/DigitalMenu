package com.br.digitalmenu.dto.request;

import lombok.Data;

@Data
public class RecuperacaoSenhaDTO {
    private String email;
    private String codigo;
    private String novaSenha;
}
