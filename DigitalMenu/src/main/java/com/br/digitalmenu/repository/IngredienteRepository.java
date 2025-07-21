package com.br.digitalmenu.repository;

import com.br.digitalmenu.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    List<Ingrediente> findByAtivoTrue();
}
