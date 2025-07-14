package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.InsertRestricaoDTO;
import com.br.digitalmenu.model.Restricao;
import com.br.digitalmenu.repository.RestricaoRepository;
import com.br.digitalmenu.service.RestricaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restricoes")
public class RestricaoController {

    @Autowired
    private RestricaoRepository restricaoRepository;

    @Autowired
    private RestricaoService restricaoService;

    @GetMapping
    public ResponseEntity<?> getRestricoes(){
        return ResponseEntity.ok(restricaoRepository.findAll(Sort.by("nomeRestricao").ascending()));
    }

    @PostMapping
    public ResponseEntity<?> insertRestricao(@Valid @RequestBody InsertRestricaoDTO insertRestricaoDTO){
        return restricaoService.insertRestricao(insertRestricaoDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRestricao(@RequestParam Integer idRestricao){
        return restricaoService.deleteRestricao(idRestricao);
    }
}
