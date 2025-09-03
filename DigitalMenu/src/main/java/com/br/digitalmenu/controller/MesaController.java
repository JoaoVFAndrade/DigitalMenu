package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.InsertMesaDTO;
import com.br.digitalmenu.dto.response.MesaResponseDTO;
import com.br.digitalmenu.repository.MesaRepository;
import com.br.digitalmenu.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mesa")
public class MesaController {

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private MesaService mesaService;

    @GetMapping
    public ResponseEntity<?> getMesas(){
        return ResponseEntity.ok(mesaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> insertMesa(@RequestBody @Valid InsertMesaDTO insertMesaDTO){
        return mesaService.insertMesa(insertMesaDTO);
    }


    @GetMapping("/qrCode")
    public ResponseEntity<?> gerarQrCode(@RequestParam Long idMesa){
        return mesaService.gerarQrCode(idMesa);
    }

    @PutMapping
    public ResponseEntity<?> updateMesa(@RequestBody @Valid MesaResponseDTO mesaResponseDTO){
        return mesaService.updateMesa(mesaResponseDTO);
    }
}
