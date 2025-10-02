package com.br.digitalmenu.controller;

import com.br.digitalmenu.service.RecuperacaoSenhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class RecuperacaoSenhaController {

    @Autowired
    private RecuperacaoSenhaService recuperacaoSenhaService;

    @PostMapping("/esqueci-senha")
    public ResponseEntity<Map<String, String>> esqueceuSenha(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        recuperacaoSenhaService.esqueceuSenha(email);

        Map<String, String> resposta = Map.of("mensagem", "Link de redefinição enviado para o e-mail.");
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/trocar-senha")
    public ResponseEntity<Map<String, String>> trocarSenha(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String novaSenha = request.get("novaSenha");

        recuperacaoSenhaService.trocarSenha(token, novaSenha);

        Map<String, String> resposta = Map.of("mensagem", "Senha alterada com sucesso");
        return ResponseEntity.ok(resposta);
    }
}

