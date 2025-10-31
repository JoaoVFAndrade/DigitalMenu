package com.br.digitalmenu.dto.request;

import java.util.List;

import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.validacoes.EntityExists;
import lombok.Data;

@Data
public class RestricoesClienteRequestDTO {
    private Long idCliente;
    private List<Long> restricoesParaAdicionar;
    private List<Long> restricoesParaRemover;

}
