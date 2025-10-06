package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.ProdutoRequestDTO;
import com.br.digitalmenu.dto.response.ProdutoResponseDTO;
import com.br.digitalmenu.service.CloudinaryService;
import com.br.digitalmenu.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> findAll(){
        return ResponseEntity.ok(produtoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(produtoService.findById(id));
    }

    @PostMapping ResponseEntity<ProdutoResponseDTO> criar(@RequestBody ProdutoRequestDTO dto){
        ProdutoResponseDTO responseDTO = produtoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto){
        return ResponseEntity.ok(produtoService.atualizarProduto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> desativaProduto(@PathVariable Long id){
        produtoService.desativarProduto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/foto",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFoto(
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(cloudinaryService.uploadFile(file));
    }

}
