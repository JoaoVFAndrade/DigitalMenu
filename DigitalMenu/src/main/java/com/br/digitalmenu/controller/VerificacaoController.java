package com.br.digitalmenu.controller;

import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.repository.ClienteRepository;
import com.br.digitalmenu.service.VerificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
public class VerificacaoController {
    @Autowired
    private VerificacaoService verificacaoService;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/confirmar")
    public ResponseEntity<String> confirmarEmail(@RequestParam String email, @RequestParam String codigo){
        boolean valido = verificacaoService.validarCodigo(email,codigo);

        if(!valido){
            return ResponseEntity.badRequest().body("Codigo invalido ou expirado");
        }

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));

        cliente.setEmailValidado(true);
        clienteRepository.save(cliente);

        return ResponseEntity.ok("E-mail validado com sucesso!");
    }
}
