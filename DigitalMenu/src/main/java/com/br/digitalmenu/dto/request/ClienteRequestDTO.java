package com.br.digitalmenu.dto.request;

import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.validacoes.UniqueValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteRequestDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Email(message = "E-mail inválido")
    @NotBlank(message = "O email é obrigatório")
    @UniqueValue(entity = Cliente.class, fieldName = "email")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;
}
