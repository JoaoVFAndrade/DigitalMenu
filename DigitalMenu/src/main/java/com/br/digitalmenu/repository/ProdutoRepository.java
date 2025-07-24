package com.br.digitalmenu.repository;

import com.br.digitalmenu.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
