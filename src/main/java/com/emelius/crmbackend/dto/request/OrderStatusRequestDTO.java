package com.emelius.crmbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusRequestDTO {
    @NotBlank(message = "El nuevo estatus es obligatorio")
    private String status;
}