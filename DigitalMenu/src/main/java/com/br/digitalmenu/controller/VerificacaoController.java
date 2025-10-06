package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.ConfirmacaoRequestDTO;
import com.br.digitalmenu.dto.response.ConfirmacaoResponseDTO;
import com.br.digitalmenu.dto.response.LoginResponseDTO;
import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.repository.ClienteRepository;
import com.br.digitalmenu.service.EmailService;
import com.br.digitalmenu.service.VerificacaoService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class VerificacaoController {
    @Autowired
    private VerificacaoService verificacaoService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/confirmar")
    public ResponseEntity<LoginResponseDTO> confirmarEmail(@RequestBody ConfirmacaoRequestDTO confirmacaoRequestDTO) {
        LoginResponseDTO response = verificacaoService.confirmarCodigoELogin(
                confirmacaoRequestDTO.getEmail(),
                confirmacaoRequestDTO.getCodigo()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reenviar-codigo")
    public ResponseEntity<String> reenviarCodigo(@RequestParam String email) throws MessagingException{
        String codigo = verificacaoService.reenviarCodigo(email);
        emailService.enviarCodigoVerificacao(email,codigo);
        return ResponseEntity.ok("Novo codigo enviado para o e-mail");
    }
}
