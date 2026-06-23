package com.emelius.crmbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UnifiedOrderRequestDTO {

    // --- SECCIÓN CLIENTE ---
    private String existingCustomerId;
    private String newCustomerName;
    private String newCustomerPhone;
    private String newCustomerEmail;

    // --- SECCIÓN INSTRUMENTO ---
    private String existingInstrumentId;
    private String newInstrumentBrand;
    private String newInstrumentModel;
    private String newInstrumentType;

    // --- SECCIÓN ÓRDEN DE SERVICIO ---
    @NotBlank(message = "El tipo de servicio es obligatorio")
    private String serviceType;
    private String initialNotes;

    // --- SECCIÓN INTAKE CONDITIONS (Estado de Recepción) ---
    private String stringGauge;
    private String action1stFret;
    private String action12thFret;
    private String paintCondition;
    private String fretboardStatus;
    private String hardwareStatus;
}