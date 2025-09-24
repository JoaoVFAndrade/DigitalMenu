package com.br.digitalmenu.service;

import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.repository.ClienteRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private final Map<String, RecuperacaoService.CodigoInfo> codigos = new ConcurrentHashMap<>();

    public void enviarCodigoRecuperacao(String email) throws MessagingException {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail nao encontrado"));

        String codigo = verificacaoService.gerarCodigo(email);
        emailService.enviarCodigoRecuperacaoSenha(cliente.getEmail(), codigo);
    }

    public boolean validarCodigo(String email, String codigo) {
        CodigoInfo info = codigos.get(email);
        if (info == null) return false;

        if (info.expira.isBefore(LocalDateTime.now())) {
            codigos.remove(email);
            return false;
        }

        boolean valido = info.codigo.equals(codigo);
        if (valido) {
            codigos.remove(email);
        }
        return valido;
    }

    public void removerCodigo(String email) {
        codigos.remove(email);
    }

    public void redefinirSenha(String email, String novaSenha) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail nao encontrado"));

        cliente.setSenha(passwordEncoder.encode(novaSenha));
        clienteRepository.save(cliente);
        verificacaoService.removerCodigo(email);
    }

    private record CodigoInfo(String codigo, LocalDateTime expira, LocalDateTime ultimoEnvio) {}
}
