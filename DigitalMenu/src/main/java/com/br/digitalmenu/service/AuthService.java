package com.br.digitalmenu.service;

import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.model.Funcionario;
import com.br.digitalmenu.repository.ClienteRepository;
import com.br.digitalmenu.repository.FuncionarioRepository;
import com.br.digitalmenu.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;

    public AuthService(FuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder, ClienteRepository clienteRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.clienteRepository = clienteRepository;
    }

    public String loginFuncionario(String email, String senha) {
        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        // compara a senha digitada com a hash do banco
        if (!passwordEncoder.matches(senha, funcionario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        return JwtUtil.generateToken(funcionario.getEmail(), funcionario.getRoles());
    }

    public String loginCliente(String email, String senha) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (!passwordEncoder.matches(senha, cliente.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }
        // cliente sempre tera role fixa "CLIENTE"
        return JwtUtil.generateToken(cliente.getEmail(), List.of("CLIENTE"));
    }
}
