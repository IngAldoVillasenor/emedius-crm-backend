package com.emelius.crmbackend.entity.tenant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "service_orders")
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @Column(nullable = false)
    private String status = "RECIBIDO";

    @Column(name = "service_type", nullable = false)
    private String serviceType;

    @Column(name = "specific_requests", columnDefinition = "TEXT")
    private String specificRequests;

    @CreationTimestamp
    @Column(name = "received_date", updatable = false)
    private LocalDateTime receivedDate;

    @Column(name = "estimated_delivery_date")
    private LocalDateTime estimatedDeliveryDate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relación 1 a 1: Una orden tiene un solo registro de condiciones de entrada
    @OneToOne(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private IntakeCondition intakeCondition;
}