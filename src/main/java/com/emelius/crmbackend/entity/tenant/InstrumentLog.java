package com.emelius.crmbackend.entity.tenant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "instrument_logs")
public class InstrumentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Relación: Muchas notas pertenecen a un solo instrumento
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    // Guardamos el ID de la orden de servicio como referencia (opcional)
    // Así sabemos en qué visita exacta se hizo esta anotación
    @Column(name = "service_order_id")
    private String serviceOrderId;

    @Column(nullable = false)
    private String author; // Ej. "Emedius", "Carlos (Ayudante)"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String note;

    // TEXTO LIBRE: Permite "Diagnóstico", "Espera de Piezas", "Nota General", etc.
    private String type;

    @Column(name = "entry_date", updatable = false)
    private LocalDateTime entryDate;

    // Sella la fecha exacta antes de guardar en PostgreSQL
    @PrePersist
    protected void onCreate() {
        this.entryDate = LocalDateTime.now();
    }
}