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

    public void enviarCodigoVerificacao(String email, String codigo) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Confirmação de E-mail - DigitalMenu");

        String conteudoHtml = """
                <html>
                  <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                
                      <div style="text-align: center;">
                        <img src="cid:logoDigitalMenu" alt="Logo DigitalMenu" style="width: 150px; margin-bottom: 20px;">
                      </div>
                
                      <h2 style="color: #E45E25; text-align: center;">Confirmação de E-mail</h2>
                      <p>Olá,</p>
                      <p>Recebemos o seu cadastro no <strong>DigitalMenu</strong> e precisamos confirmar o seu e-mail.</p>
                      <p style="font-size: 16px;">O seu código de verificação é:</p>
                      <h1 style="color: #ffffff; background: #E45E25; padding: 10px; text-align: center; border-radius: 5px;">%s</h1>
                      <p>⚠️ Este código é válido por apenas <strong>5 minutos</strong>.</p>
                      <p>Se você não realizou este cadastro, por favor ignore esta mensagem.</p>
                      <br>
                      <p style="text-align: center; color: #777;">Equipe DigitalMenu</p>
                    </div>
                  </body>
                </html>
                """.formatted(codigo);

        helper.setText(conteudoHtml, true);

        ClassPathResource logo = new ClassPathResource("static/logo.png");
        helper.addInline("logoDigitalMenu", logo);

        mailSender.send(message);
    }

    public void enviarCodigoRecuperacaoSenha(String email, String codigo) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Recuperação de Senha - DigitalMenu");

        String conteudoHtml = """
                <html>
                  <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                
                      <div style="text-align: center;">
                        <img src="cid:logoDigitalMenu" alt="Logo DigitalMenu" style="width: 150px; margin-bottom: 20px;">
                      </div>
                
                      <h2 style="color: #E45E25; text-align: center;">Recuperação de Senha</h2>
                      <p>Olá,</p>
                      <p>Recebemos uma solicitação para redefinir a sua senha no <strong>DigitalMenu</strong>.</p>
                      <p style="font-size: 16px;">O seu código de recuperação é:</p>
                      <h1 style="color: #ffffff; background: #E45E25; padding: 10px; text-align: center; border-radius: 5px;">%s</h1>
                      <p>⚠️ Este código é válido por apenas <strong>5 minutos</strong>.</p>
                      <p>Se você não solicitou a recuperação, ignore este e-mail. Sua senha permanecerá inalterada.</p>
                      <br>
                      <p style="text-align: center; color: #777;">Equipe DigitalMenu</p>
                    </div>
                  </body>
                </html>
                """.formatted(codigo);

        helper.setText(conteudoHtml, true);

        ClassPathResource logo = new ClassPathResource("static/logo.png");
        helper.addInline("logoDigitalMenu", logo);

        mailSender.send(message);
    }
}