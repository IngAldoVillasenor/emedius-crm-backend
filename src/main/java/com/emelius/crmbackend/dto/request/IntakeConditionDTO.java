package com.emelius.crmbackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntakeConditionDTO {
    private String stringGauge;
    private String action1stFret;
    private String action12thFret;
    private String paintCondition;
    private String fretboardStatus;
    private String hardwareStatus;
}