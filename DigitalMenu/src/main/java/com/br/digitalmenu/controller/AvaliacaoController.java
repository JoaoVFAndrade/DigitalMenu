package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.InsertAvaliacaoDTO;
import com.br.digitalmenu.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/avaliacao")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping("/insert")
    public ResponseEntity<?> insertAvaliacao(@RequestBody @Valid InsertAvaliacaoDTO dto){
        return avaliacaoService.insertAvaliacao(dto);
    }

    @GetMapping
    public ResponseEntity<?> getAvaliacoes(
            @RequestParam (required = false) LocalDate dataInicial,
            @RequestParam (required = false) LocalDate dataFinal){
        return avaliacaoService.getAvaliacoes(dataInicial,dataFinal);
    }
}
