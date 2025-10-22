package com.br.digitalmenu.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteUpdateDTO {
    private String nome;

    @Email(message = "E-mail inválido")
    private String email;

    private String senha;

    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;
}
