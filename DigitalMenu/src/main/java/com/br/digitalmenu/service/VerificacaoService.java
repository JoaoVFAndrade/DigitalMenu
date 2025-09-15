package com.br.digitalmenu.service;

import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public boolean validarCodigo(String email, String codigoDigitado) {
        CodigoInfo info = codigos.get(email);
        if (info == null) return false;

        if (info.expira.isBefore(LocalDateTime.now())) {
            codigos.remove(email);
            return false;
        }

        boolean valido = info.codigo.equals(codigoDigitado);
        if (valido) {
            codigos.remove(email);
        }
        return valido;
    }

    public void removerCodigo(String email) {
        codigos.remove(email);
    }

    private record CodigoInfo(String codigo, LocalDateTime expira, LocalDateTime ultimoEnvio) {}
}
