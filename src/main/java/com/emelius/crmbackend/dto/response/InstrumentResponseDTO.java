package com.emelius.crmbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InstrumentResponseDTO {
    private UUID id;
    private UUID customerId; // Útil para que el frontend sepa a quién pertenece
    private String type;
    private String brand;
    private String model;
    private String serialNumber;
    private String customerName;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}