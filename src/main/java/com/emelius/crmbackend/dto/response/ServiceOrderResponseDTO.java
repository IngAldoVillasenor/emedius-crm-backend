package com.emelius.crmbackend.dto.response;

import com.emelius.crmbackend.dto.request.IntakeConditionDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ServiceOrderResponseDTO {
    private UUID id;
    private UUID instrumentId;
    private String serviceType;
    private String status;
    private String notes;

    // Estos son los datos "aplanados" que le faltan a la tarjeta
    private String instrumentBrand;
    private String instrumentModel;
    private String customerName;

    private String extraWorkReason;
    private Double extraCost;

    private String approvalToken;
    private LocalDateTime termsAcceptedAt;

    // Para que la fecha se muestre bien y no diga "Sin fecha"
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime entryDate;

    // Tu objeto de condiciones físicas también debería ir aquí para cuando
    // quieras hacer la pantalla de detalles de la orden en el futuro.
    private IntakeConditionDTO intakeCondition;
}