package com.br.digitalmenu.dto.response;

import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.model.Mesa;
import com.br.digitalmenu.model.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class PedidoResponseDTO {

    private Long id;

    private LocalDateTime abertoEm;

    private LocalDateTime finalizadoEm;

    private StatusPedido statusPedido;

    private BigDecimal total;

    private ClienteResponseDTO cliente;

    private MesaResponseDTO mesa;

    private List<ProdutoPedidoResponseDTO> produtosPedidoResponseDTO;


}
