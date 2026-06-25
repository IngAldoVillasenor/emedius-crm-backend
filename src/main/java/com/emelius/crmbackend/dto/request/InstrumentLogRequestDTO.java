package com.emelius.crmbackend.dto.request;

import lombok.Data;

@Data
public class InstrumentLogRequestDTO {
    private String instrumentId;
    private String serviceOrderId; // Puede venir null si es una nota suelta
    private String author;
    private String note;
    private String type;
}