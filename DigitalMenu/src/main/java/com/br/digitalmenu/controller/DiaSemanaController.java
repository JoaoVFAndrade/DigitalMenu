package com.br.digitalmenu.controller;

import com.br.digitalmenu.repository.DiaSemanaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diaSemana")
public class DiaSemanaController {

    @Autowired
    private DiaSemanaRepository diaSemanaRepository;

    @GetMapping
    public ResponseEntity<?> getDiasSemana(){
        return ResponseEntity.ok(diaSemanaRepository.findAll());
    }
}
