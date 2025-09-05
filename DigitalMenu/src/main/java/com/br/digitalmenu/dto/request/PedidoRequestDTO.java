package com.br.digitalmenu.dto.request;

import com.br.digitalmenu.model.StatusPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PedidoRequestDTO {

    private LocalDateTime abertoEm;

    private LocalDateTime finalizadoEm;

    private StatusPedido statusPedido;

    private BigDecimal total;
}
