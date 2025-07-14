package com.br.digitalmenu.service;

import com.br.digitalmenu.model.Categoria;
import com.br.digitalmenu.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> findAll(){
        return categoriaRepository.findAll();
    }

    public List<Categoria> findAllAtivos(){
        return categoriaRepository.findByActiveTrue();
    }

    public Optional<Categoria> findById(Long id){
        return categoriaRepository.findById(id);
    }

    public Categoria save(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    public void delete(Long id){
        categoriaRepository.findById(id).map(categoria -> {
            categoria.setAtivo(false);
            return categoriaRepository.save(categoria);
        }).orElseThrow(() -> new RuntimeException("Categoria nao encontrada com id " + id));
    }
}
