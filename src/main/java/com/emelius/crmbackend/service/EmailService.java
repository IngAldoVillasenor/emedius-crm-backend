package com.emelius.crmbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendReadyNotification(String toEmail, String customerName, String instrumentName, String folio) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("¡Tu instrumento está listo! 🎸 - Emedius' Workshop");

        String body = "Hola " + customerName + ",\n\n"
                + "Te informamos que el servicio para tu " + instrumentName + " (Folio: #" + folio + ") ha concluido exitosamente y ya está listo para ser entregado.\n"
                + "Por favor, agenda el día y hora para recogerla en el siguiente enlace: https://calendly.com/emediusgw/entrega\n\n"
                + "Puedes pasar al taller a recogerlo dentro de nuestros horarios de atención. Recuerda llevar tu ticket de recepción.\n\n"
                + "¡Gracias por confiar en nosotros!\n"
                + "Atte: Emedius' Guitar Workshop";

        message.setText(body);

        try {
            mailSender.send(message);
            System.out.println("Correo enviado exitosamente a: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}