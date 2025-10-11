package com.br.digitalmenu.dto.request;

import java.util.List;

import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.validacoes.EntityExists;

public record RestricoesClienteRequestDTO(
    @EntityExists(entityClass=Cliente.class)
    Long idCliente,
    List<Long> idRestricoes
) {

}
