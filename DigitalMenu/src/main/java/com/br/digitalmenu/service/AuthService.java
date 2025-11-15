package com.br.digitalmenu.service;

import com.br.digitalmenu.exception.ResourceNotFoundException;
import com.br.digitalmenu.exception.TentativasDeLoginException;
import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.model.Funcionario;
import com.br.digitalmenu.repository.ClienteRepository;
import com.br.digitalmenu.repository.FuncionarioRepository;
import com.br.digitalmenu.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(FuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder, ClienteRepository clienteRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.clienteRepository = clienteRepository;
    }

    public String loginFuncionario(String email, String senha) {
        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));

        if(funcionario.getTentativasDeLoginFracasada()>=3){
            throw new TentativasDeLoginException("Tentativas de login excedidas, redefina sua senha");
        }

        // compara a senha digitada com a hash do banco
        if (!passwordEncoder.matches(senha, funcionario.getSenha())) {
            funcionario.setTentativasDeLoginFracasada((byte) (funcionario.getTentativasDeLoginFracasada()+1));
            throw new RuntimeException("Senha inválida");
        }

        return JwtUtil.generateToken(funcionario.getEmail(), funcionario.getRoles());
    }

    public String loginCliente(String email, String senha) {
        logger.info("Login attempt - email=[{}] senhaRecebida.length=[{}]", email, senha == null ? 0 : senha.length());
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        logger.info("Hash do DB para email [{}] = {}", email, cliente.getSenha());

        String senhaTrim = senha == null ? "" : senha.trim();
        boolean matches = passwordEncoder.matches(senhaTrim, cliente.getSenha());
        logger.info("PasswordEncoder.matches -> {}", matches);

        if(cliente.getTentativasDeLoginFracasada()>=3){
            throw new TentativasDeLoginException("Tentativas de login excedidas, redefina sua senha");
        }

        if (!passwordEncoder.matches(senha, cliente.getSenha())) {
            cliente.setTentativasDeLoginFracasada((byte) (cliente.getTentativasDeLoginFracasada()+ (byte) 1));
            clienteRepository.save(cliente);
            throw new RuntimeException("Senha inválida");
        }
        // cliente sempre tera role fixa "CLIENTE"
        return JwtUtil.generateToken(cliente.getEmail(), List.of("CLIENTE"));
    }
}
