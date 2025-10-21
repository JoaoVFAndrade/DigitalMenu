package com.br.digitalmenu.dto.response;

import com.br.digitalmenu.dto.RestricaoDTO;
import com.br.digitalmenu.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private List<RestricaoDTO> restricoes;

    public ClienteResponseDTO() {

    }

    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getIdCliente();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.dataNascimento = cliente.getDataNascimento();
        this.restricoes = cliente.getRestricoes().stream().map(restricao -> new RestricaoDTO(restricao.getIdRestricao(), restricao.getNomeRestricao(), restricao.getTipoRestricao())).toList();
    }
}
