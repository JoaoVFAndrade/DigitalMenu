package com.br.digitalmenu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificacaoService verificacaoService;

    public void enviarCodigoVerificacao(String email){
        String codigo = verificacaoService.gerarCodigo(email);


        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(email);
        mensagem.setSubject("Código de Verificação");
        mensagem.setText("Olá, seu código de verificação é: " + codigo);
        mailSender.send(mensagem);
    }

}
