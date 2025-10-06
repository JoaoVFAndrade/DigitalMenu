package com.br.digitalmenu.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificacaoService verificacaoService;

    private void enviarEmail(String email, String assunto, String titulo, String mensagem, String conteudoExtra) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject(assunto);

        String conteudoHtml = """
                <html>
                <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                        <div style="text-align: center;">
                            <img src="cid:logoDigitalMenu" alt="Logo DigitalMenu" style="width: 150px; margin-bottom: 20px;">
                        </div>
                        <h2 style="color: #E45E25; text-align: center;">%s</h2>
                        <p>Olá,</p>
                        <p>%s</p>
                        %s
                        <br>
                        <p style="text-align: center; color: #777;">Equipe DigitalMenu</p>
                    </div>
                </body>
                </html>
                """.formatted(titulo, mensagem, conteudoExtra);

        helper.setText(conteudoHtml, true);

        ClassPathResource logo = new ClassPathResource("static/logo.png");
        helper.addInline("logoDigitalMenu", logo);

        mailSender.send(message);
    }


    public void enviarCodigoVerificacao(String email, String codigo) throws MessagingException {
        String conteudoExtra = """
                    <p style="font-size: 16px;">O seu código é:</p>
                    <h1 style="color: #ffffff; background: #E45E25; padding: 10px; text-align: center; border-radius: 5px;">%s</h1>
                    <p>⚠️ Este código é válido por apenas <strong>5 minutos</strong>.</p>
                """.formatted(codigo);

        enviarEmail(email, "Confirmação de E-mail - DigitalMenu", "Confirmação de E-mail", "Recebemos o seu cadastro no <strong>DigitalMenu</strong> e precisamos confirmar o seu e-mail.", conteudoExtra);
    }

    public void enviarCodigoRecuperacaoSenha(String email, String link) throws MessagingException {
        String conteudoExtra = """
                    <p style="font-size: 16px;">Clique no botão abaixo para redefinir sua senha:</p>
                    <p style="text-align: center; margin-top: 20px;">
                        <a href="%s" style="color: #ffffff; background: #E45E25; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block">Redefinir Senha</a>
                    </p>
                """.formatted(link);

        enviarEmail(email, "Recuperação de Senha - DigitalMenu", "Recuperação de Senha", "Recebemos uma solicitação para redefinir a sua senha no <strong>DigitalMenu</strong>. Se não foi você, ignore este e-mail.", conteudoExtra);
    }
}