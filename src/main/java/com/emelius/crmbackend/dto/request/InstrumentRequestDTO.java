package com.emelius.crmbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InstrumentRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private UUID customerId;

    @NotBlank(message = "El tipo de instrumento es obligatorio (ej. Eléctrica, Bajo)")
    private String type;

    @NotBlank(message = "La marca es obligatoria")
    private String brand;

    private String model;
    private String serialNumber;
}