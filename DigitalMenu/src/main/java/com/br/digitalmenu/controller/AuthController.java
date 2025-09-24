package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.LoginRequest;
import com.br.digitalmenu.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/funcionario/login")
    public ResponseEntity<String> loginFuncionario(@RequestBody LoginRequest loginRequest) {
        String token = authService.loginFuncionario(loginRequest.getEmail(), loginRequest.getSenha());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/cliente/login")
    public ResponseEntity<String> loginCliente(@RequestBody LoginRequest loginRequest) {
        String token = authService.loginCliente(loginRequest.getEmail(), loginRequest.getSenha());
        return ResponseEntity.ok(token);
    }
}
