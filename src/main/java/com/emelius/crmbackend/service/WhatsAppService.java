package com.emelius.crmbackend.service;

import com.emelius.crmbackend.entity.tenant.Customer;
import com.emelius.crmbackend.entity.tenant.Instrument;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    public void sendReadyNotification(Customer customer, Instrument instrument) {
        String calendlyLink = "https://calendly.com/emelius-workshop/entrega";

        String message = String.format(
                "¡Hola %s! 🎸 Te avisamos que tu %s %s ya quedó al 100%% y está lista para entrega. " +
                        "Por favor, agenda el día y hora para recogerla en el siguiente enlace: %s",
                customer.getName(),
                instrument.getBrand(),
                instrument.getModel(),
                calendlyLink
        );

        // Aquí iría la llamada HTTP real a la API de WhatsApp
        System.out.println("========================================");
        System.out.println("Enviando WhatsApp al número: " + customer.getWhatsappNumber());
        System.out.println("Mensaje: " + message);
        System.out.println("========================================");
    }
}