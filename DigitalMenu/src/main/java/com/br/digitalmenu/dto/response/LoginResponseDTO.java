package com.br.digitalmenu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String nome;
    private Long id;
}
