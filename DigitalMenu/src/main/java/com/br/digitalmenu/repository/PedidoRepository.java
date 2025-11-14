package com.br.digitalmenu.repository;

import com.br.digitalmenu.model.Pedido;
import com.br.digitalmenu.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatusPedido(StatusPedido statusPedido);

    List<Pedido> findByCliente_IdCliente(Long idCliente);

    @Query("SELECT p FROM Pedido p WHERE FUNCTION('DATE', p.finalizadoEm) = :dataEspecifica")
    List<Pedido> findByFinalizadoEmData(LocalDate dataEspecifica);

    boolean existsByCliente_IdClienteAndStatusPedido(Long idCliente, StatusPedido statusPedido);


}
