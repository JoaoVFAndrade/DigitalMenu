package com.br.digitalmenu.controller;

import com.br.digitalmenu.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/funcionario/login")
    public ResponseEntity<String> loginFuncionario(@RequestParam String username,
                                                   @RequestParam String password) {
        String token = authService.loginFuncionario(username, password);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/cliente/login")
    public ResponseEntity<String> loginCliente(@RequestParam String email,
                                               @RequestParam String senha) {
        String token = authService.loginCliente(email, senha);
        return ResponseEntity.ok(token);
    }
}
