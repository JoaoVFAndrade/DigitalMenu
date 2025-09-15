package com.br.digitalmenu.service;

import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.repository.ClienteRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RecuperacaoService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VerificacaoService verificacaoService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void enviarCodigoRecuperacao(String email) throws MessagingException {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail nao encontrado"));

        String codigo = verificacaoService.gerarCodigo(email);
        emailService.enviarCodigoRecuperacaoSenha(cliente.getEmail(), codigo);
    }

    public boolean validarCodigo(String email, String codigo) {
        return verificacaoService.validarCodigo(email, codigo);
    }

    public void redefinirSenha(String email, String novaSenha) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail nao encontrado"));

        cliente.setSenha(passwordEncoder.encode(novaSenha));
        clienteRepository.save(cliente);
        verificacaoService.removerCodigo(email);
    }
}
