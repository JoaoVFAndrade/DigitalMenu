package com.br.digitalmenu.repository;

import com.br.digitalmenu.model.Pedido;
import com.br.digitalmenu.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatusPedido(StatusPedido statusPedido);
}
