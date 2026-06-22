package com.emelius.crmbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ServiceOrderRequestDTO {

    @NotNull(message = "El ID del instrumento es obligatorio")
    private UUID instrumentId;

    // 1. Agregamos el campo que faltaba para recibir el paquete elegido
    @NotNull(message = "El tipo de servicio es obligatorio")
    private String serviceType;

    // 2. Le decimos a Spring: "El Frontend te va a mandar un campo llamado 'notes',
    // guárdalo en esta variable llamada 'specificRequests'".
    @JsonProperty("notes")
    private String specificRequests;

    @NotNull(message = "Las condiciones de recepción son obligatorias")
    private IntakeConditionDTO intakeCondition;
}