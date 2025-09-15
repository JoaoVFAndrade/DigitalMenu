package com.br.digitalmenu.controller;


import com.br.digitalmenu.dto.request.CategoriaRequestDTO;
import com.br.digitalmenu.dto.response.CategoriaResponseDTO;
import com.br.digitalmenu.model.Categoria;
import com.br.digitalmenu.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/all")
    public List<CategoriaResponseDTO> getAllCategorias(){
        return categoriaService.findAll();
    }

    @GetMapping("/ativos")
    public List<CategoriaResponseDTO> getAllAtivos(){
        return categoriaService.findAllAtivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(categoriaService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> create(@RequestBody @Valid CategoriaRequestDTO dto){
        CategoriaResponseDTO responseDTO = categoriaService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> ativaCategoria(@PathVariable @Valid Long id){
        try{
            categoriaService.ativaCategoria(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
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

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> update(@PathVariable Long id, @RequestBody @Valid CategoriaRequestDTO dto) {
        try {
            return ResponseEntity.ok(categoriaService.atualizaCategoria(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
