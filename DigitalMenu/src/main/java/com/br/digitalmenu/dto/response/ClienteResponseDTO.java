package com.br.digitalmenu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;

    public ClienteResponseDTO() {

    }
}
