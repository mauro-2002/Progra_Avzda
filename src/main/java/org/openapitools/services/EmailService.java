package org.openapitools.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String destinatario, String asunto, String contenido) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(contenido);
        mensaje.setFrom("seguridad_ciudadana_COL@gmail.com");

        mailSender.send(mensaje);
    }
}
