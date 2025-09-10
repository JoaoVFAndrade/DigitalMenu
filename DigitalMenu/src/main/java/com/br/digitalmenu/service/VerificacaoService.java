package com.br.digitalmenu.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class VerificacaoService {
    private final Map<String, CodigoInfo> codigos = new ConcurrentHashMap<>();

    public String gerarCodigo(String email) {
        String codigo = String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
        CodigoInfo info = new CodigoInfo(codigo, LocalDateTime.now().plusMinutes(5));
        codigos.put(email, info);
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

    private record CodigoInfo(String codigo, LocalDateTime expira) {}
}
//@TODO fazer o renvio caso o cliente nao consiga confirmar nos seus 5 min