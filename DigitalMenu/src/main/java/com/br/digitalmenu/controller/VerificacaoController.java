package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.ConfirmacaoRequestDTO;
import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.repository.ClienteRepository;
import com.br.digitalmenu.service.VerificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class VerificacaoController {
    @Autowired
    private VerificacaoService verificacaoService;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/confirmar")
    public ResponseEntity<String> confirmarEmail(@RequestBody ConfirmacaoRequestDTO confirmacaoRequestDTO){
        String token = verificacaoService.confirmarCodigoELogin(
                confirmacaoRequestDTO.getEmail(),
                confirmacaoRequestDTO.getCodigo()
        );

        return ResponseEntity.ok(token);
    }
}
