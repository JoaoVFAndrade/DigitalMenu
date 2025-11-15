package com.br.digitalmenu.repository;

import com.br.digitalmenu.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa,Long> {

    List<Mesa> findByAtivoTrue();
}
