package com.emelius.crmbackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequestDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String name;

    private String whatsappNumber;

    @Email(message = "El formato del correo no es válido")
    private String email;
}