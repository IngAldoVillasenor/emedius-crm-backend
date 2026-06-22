package com.emelius.crmbackend.entity.tenant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "intake_conditions")
public class IntakeCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id", nullable = false)
    private ServiceOrder serviceOrder;

    @Column(name = "string_gauge")
    private String stringGauge;

    @Column(name = "action_1st_fret")
    private String action1stFret;

    @Column(name = "action_12th_fret")
    private String action12thFret;

    @Column(name = "paint_condition", columnDefinition = "TEXT")
    private String paintCondition;

    @Column(name = "fretboard_status", columnDefinition = "TEXT")
    private String fretboardStatus;

    @Column(name = "hardware_status", columnDefinition = "TEXT")
    private String hardwareStatus;
}