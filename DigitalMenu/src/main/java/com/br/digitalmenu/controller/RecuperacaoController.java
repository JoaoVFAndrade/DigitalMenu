package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.RecuperacaoSenhaDTO;
import com.br.digitalmenu.service.RecuperacaoService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recuperacao")
public class RecuperacaoController {

    @Autowired
    private RecuperacaoService recuperacaoService;

    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> esqueciSenha(@RequestBody RecuperacaoSenhaDTO recuperacaoSenhaDTO) throws MessagingException{
        recuperacaoService.enviarCodigoRecuperacao(recuperacaoSenhaDTO.getEmail());
        return ResponseEntity.ok("Codigo enviado para o e-mail");
    }

    @PostMapping("/validar-codigo")
    public ResponseEntity<String> validarCodigo(@RequestBody RecuperacaoSenhaDTO recuperacaoSenhaDTO){
        boolean valido = recuperacaoService.validarCodigo(recuperacaoSenhaDTO.getEmail(), recuperacaoSenhaDTO.getCodigo());
        if(!valido){
            return ResponseEntity.badRequest().body("Codigo invalido");
        }
        return ResponseEntity.ok("Codigo valido");
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody RecuperacaoSenhaDTO recuperacaoSenhaDTO){
        recuperacaoService.redefinirSenha(recuperacaoSenhaDTO.getEmail(), recuperacaoSenhaDTO.getNovaSenha());
        return ResponseEntity.ok("Senha redefinida com sucesso");
    }
}
