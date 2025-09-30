package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.LoginRequest;
import com.br.digitalmenu.dto.response.LoginResponseDTO;
import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.repository.ClienteRepository;
import com.br.digitalmenu.service.AuthService;
import com.br.digitalmenu.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final ClienteRepository clienteRepository;

    public AuthController(AuthService authService, ClienteRepository clienteRepository) {
        this.authService = authService;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping("/funcionario/login")
    public ResponseEntity<String> loginFuncionario(@RequestBody LoginRequest loginRequest) {
        String token = authService.loginFuncionario(loginRequest.getEmail(), loginRequest.getSenha());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/cliente/login")
    public ResponseEntity<LoginResponseDTO> loginCliente(@RequestBody LoginRequest loginRequest) {
        String token = authService.loginCliente(loginRequest.getEmail(), loginRequest.getSenha());

        Cliente cliente = clienteRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        LoginResponseDTO response = new LoginResponseDTO(token, cliente.getNome());

        return ResponseEntity.ok(response);
    }
}
