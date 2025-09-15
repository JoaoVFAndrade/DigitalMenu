package com.br.digitalmenu.controller;

import com.br.digitalmenu.service.RecuperacaoService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recuperacao")
public class RecuperacaoController {

    @Autowired
    private RecuperacaoService recuperacaoService;

    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> esqueciSenha(@RequestParam String email) throws MessagingException{
        recuperacaoService.enviarCodigoRecuperacao(email);
        return ResponseEntity.ok("Codigo enviado para o e-mail");
    }

    @PostMapping("/validar-codigo")
    public ResponseEntity<String> validarCodigo(@RequestParam String email, @RequestParam String codigo){
        boolean valido = recuperacaoService.validarCodigo(email,codigo);
        if(!valido){
            return ResponseEntity.badRequest().body("Codigo invalido");
        }
        return ResponseEntity.ok("Codigo valido");
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestParam String email, @RequestParam String novaSenha){
        recuperacaoService.redefinirSenha(email,novaSenha);
        return ResponseEntity.ok("Senha redefinida com sucesso");
    }
}
