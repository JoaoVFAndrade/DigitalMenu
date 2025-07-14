package com.br.digitalmenu.repository;

import com.br.digitalmenu.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByActiveTrue();
}
