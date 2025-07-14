package com.br.digitalmenu.controller;


import com.br.digitalmenu.model.Categoria;
import com.br.digitalmenu.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> getAllCategorias(){
        return categoriaService.findAll();
    }

    @GetMapping
    public List<Categoria> getAllAtivos(){
        return categoriaService.findAllAtivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id){
        return categoriaService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    Categoria criaCategoria(@RequestBody Categoria category){
        return categoriaService.save(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativaCategoria(@PathVariable Long id){
        try {
            categoriaService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}
