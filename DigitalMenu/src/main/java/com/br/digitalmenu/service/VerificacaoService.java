package com.br.digitalmenu.service;

import com.br.digitalmenu.exception.BadRequestException;
import com.br.digitalmenu.exception.ResourceNotFoundException;
import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.repository.ClienteRepository;
import com.br.digitalmenu.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class VerificacaoService {
    @Autowired
    private ClienteRepository clienteRepository;

    private final Map<String, CodigoInfo> codigos = new ConcurrentHashMap<>();

    public String gerarCodigo(String email) {
        CodigoInfo info = codigos.get(email);
        LocalDateTime agora = LocalDateTime.now();

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado"));
        if (cliente.isEmailValidado()) {
            throw new RuntimeException("E-mail já confirmado");
        }

        if (info != null && agora.isBefore(info.ultimoEnvio.plusMinutes(5))) {
            return info.codigo;
        }

        String codigo = String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
        CodigoInfo novoInfo = new CodigoInfo(codigo, agora.plusMinutes(5), agora);
        codigos.put(email, novoInfo);
        return codigo;
    }

    public String confirmarCodigoELogin(String email, String codigoDigitado) {
        CodigoInfo info = codigos.get(email);
        if (info == null) {
            throw new ResourceNotFoundException("Código não encontrado para o e-mail: " + email);
        }

        if (info.expira.isBefore(LocalDateTime.now())) {
            codigos.remove(email);
            throw new BadRequestException("Código expirado");
        }

        if (!info.codigo.equals(codigoDigitado)) {
            throw new BadRequestException("Código inválido");
        }

        codigos.remove(email);

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        cliente.setEmailValidado(true);
        clienteRepository.save(cliente);

        return JwtUtil.generateToken(cliente.getEmail(), List.of("CLIENTE"));
    }

    public void removerCodigo(String email) {
        codigos.remove(email);
    }

    private record CodigoInfo(String codigo, LocalDateTime expira, LocalDateTime ultimoEnvio) {}
}
