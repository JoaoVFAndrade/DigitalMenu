package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.IngredienteRequestDTO;
import com.br.digitalmenu.dto.response.IngredienteResponseDTO;
import com.br.digitalmenu.model.Ingrediente;
import com.br.digitalmenu.service.IngredienteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredientes")
public class IngredienteController {
    @Autowired
    private IngredienteService ingredienteService;

    @Operation(summary = "Lista todos os ingredientes")
    @GetMapping("/all")
    public List<IngredienteResponseDTO> getAllIngrediente() {
        return ingredienteService.findAll();
    }

    @Operation(summary = "Lista todos os ativos")
    @GetMapping("/ativos")
    public List<IngredienteResponseDTO> getAllAtivos() {
        return ingredienteService.findAllAtivos();
    }

    @Operation(summary = "Lista pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<IngredienteResponseDTO> getByid(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ingredienteService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cria um ingrediente")
    @PostMapping
    public ResponseEntity<IngredienteResponseDTO> create(@RequestBody @Valid IngredienteRequestDTO ingredienteRequestDTO) {
        return ResponseEntity.ok(ingredienteService.save(ingredienteRequestDTO));
    }

    @Operation(summary = "Ativa um ingrediente")
    @PatchMapping("/{id}")
    public ResponseEntity<IngredienteResponseDTO> ativaIngrediente(@PathVariable @Valid Long id) {
        try {
            ingredienteService.ativaIngrediente(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Desativa um ingrediente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativaIngrediente(@PathVariable Long id) {
        try {
            ingredienteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualiza ingrediente")
    @PutMapping("/{id}")
    public ResponseEntity<IngredienteResponseDTO> atualizaIngredientes(@PathVariable Long id, @RequestBody @Valid IngredienteRequestDTO ingredienteRequestDTO){
        try{
            return ResponseEntity.ok(ingredienteService.atualizaIngrediente(id, ingredienteRequestDTO));
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}
