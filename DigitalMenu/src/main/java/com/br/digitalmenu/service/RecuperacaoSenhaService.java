package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.request.ResetTokenInfo;
import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.model.Funcionario;
import com.br.digitalmenu.repository.ClienteRepository;
import com.br.digitalmenu.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecuperacaoSenhaService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private Map<String, ResetTokenInfo> resetTokens = new HashMap<>();

    public void esqueceuSenha(String email) {
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(email);
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findByEmail(email);

        if (clienteOpt.isEmpty() && funcionarioOpt.isEmpty()) {
            throw new RuntimeException("E-mail não encontrado");
        }

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, new ResetTokenInfo(email, LocalDateTime.now().plusMinutes(15)));

        String link = "http://localhost:4200/trocar-senha?token=" + token;

        try {
            emailService.enviarCodigoRecuperacaoSenha(email, link);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    public void trocarSenha(String token, String novaSenha){
        ResetTokenInfo info = resetTokens.get(token);
        if (info == null || info.getExpira().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token inválido ou expirado.");
        }

        String email = info.getEmail();

        // tenta achar cliente
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(email);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setSenha(passwordEncoder.encode(novaSenha));
            clienteRepository.save(cliente);
            resetTokens.remove(token);
            return;
        }

        // tenta achar funcionario
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findByEmail(email);
        if (funcionarioOpt.isPresent()) {
            Funcionario funcionario = funcionarioOpt.get();
            funcionario.setSenha(passwordEncoder.encode(novaSenha));
            funcionarioRepository.save(funcionario);
            resetTokens.remove(token);
            return;
        }

        throw new RuntimeException("Usuário não encontrado");
    }
}