package com.br.digitalmenu.repository;

import com.br.digitalmenu.model.ProdutoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProdutoPedidoRepository extends JpaRepository<ProdutoPedido,Long > {
    List<ProdutoPedido> findByData(LocalDate data);


}
