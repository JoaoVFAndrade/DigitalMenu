package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.LoginRequest;
import com.br.digitalmenu.dto.response.LoginResponseDTO;
import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.model.Funcionario;
import com.br.digitalmenu.repository.ClienteRepository;
import com.br.digitalmenu.repository.FuncionarioRepository;
import com.br.digitalmenu.service.AuthService;
import com.br.digitalmenu.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;

    public AuthController(AuthService authService, ClienteRepository clienteRepository, FuncionarioRepository funcionarioRepository) {
        this.authService = authService;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @PostMapping("/funcionario/login")
    public ResponseEntity<LoginResponseDTO> loginFuncionario(@RequestBody LoginRequest loginRequest) {
        String token = authService.loginFuncionario(loginRequest.getEmail(), loginRequest.getSenha());
        Funcionario funcionario = funcionarioRepository.findByEmail(loginRequest.getEmail()).
                orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));
        LoginResponseDTO response = new LoginResponseDTO(token, funcionario.getNome());
        return ResponseEntity.ok(response);
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
