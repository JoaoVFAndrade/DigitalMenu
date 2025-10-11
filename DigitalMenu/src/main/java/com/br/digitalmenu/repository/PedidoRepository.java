package com.br.digitalmenu.repository;

import com.br.digitalmenu.model.Pedido;
import com.br.digitalmenu.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatusPedido(StatusPedido statusPedido);

    @Query("SELECT p FROM Pedido p WHERE FUNCTION('DATE', p.finalizadoEm) = :dataEspecifica")
    List<Pedido> findByFinalizadoEmData(LocalDate dataEspecifica);

}
