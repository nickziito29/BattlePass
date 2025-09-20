package com.battlepass.api.config; // Ou um novo pacote 'service'

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) {
        String recipientAddress = to;
        String subject = "BattlePass - Confirmação de Registro";
        String confirmationUrl = "http://localhost:8080/api/auth/verify?token=" + token; // A URL do seu backend
        String message = "Olá!\n\nObrigado por se registrar no BattlePass. Por favor, clique no link abaixo para ativar sua conta:\n\n"
                + confirmationUrl
                + "\n\nSe você não se registrou, por favor ignore este email.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}