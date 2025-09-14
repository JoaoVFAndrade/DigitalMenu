package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.IngredienteRequestDTO;
import com.br.digitalmenu.dto.response.IngredienteResponseDTO;
import com.br.digitalmenu.model.Ingrediente;
import com.br.digitalmenu.service.IngredienteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Lista pelo idMesa")
    @GetMapping("/{idMesa}")
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
        IngredienteResponseDTO responseDTO = ingredienteService.save(ingredienteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "Ativa um ingrediente")
    @PatchMapping("/{idMesa}")
    public ResponseEntity<IngredienteResponseDTO> ativaIngrediente(@PathVariable @Valid Long id) {
        try {
            ingredienteService.ativaIngrediente(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Desativa um ingrediente")
    @DeleteMapping("/{idMesa}")
    public ResponseEntity<Void> desativaIngrediente(@PathVariable Long id) {
        try {
            ingredienteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualiza ingrediente")
    @PutMapping("/{idMesa}")
    public ResponseEntity<IngredienteResponseDTO> atualizaIngredientes(@PathVariable Long id, @RequestBody @Valid IngredienteRequestDTO ingredienteRequestDTO){
        try{
            return ResponseEntity.ok(ingredienteService.atualizaIngrediente(id, ingredienteRequestDTO));
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}
