package com.emelius.crmbackend.controller;

import com.emelius.crmbackend.dto.request.InstrumentLogRequestDTO;
import com.emelius.crmbackend.entity.tenant.Instrument;
import com.emelius.crmbackend.entity.tenant.InstrumentLog;
import com.emelius.crmbackend.repository.tenant.InstrumentLogRepository;
import com.emelius.crmbackend.repository.tenant.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/instrument-logs")
public class InstrumentLogController {

    @Autowired
    private InstrumentLogRepository logRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    // 1. Añadir una nueva entrada a la bitácora
    @PostMapping
    public ResponseEntity<?> addLogEntry(@RequestBody InstrumentLogRequestDTO dto) {
        Instrument instrument = instrumentRepository.findById(UUID.fromString(dto.getInstrumentId()))
                .orElseThrow(() -> new RuntimeException("Instrumento no encontrado"));

        InstrumentLog newLog = new InstrumentLog();
        newLog.setInstrument(instrument);
        newLog.setServiceOrderId(dto.getServiceOrderId());
        newLog.setAuthor(dto.getAuthor());
        newLog.setNote(dto.getNote());
        newLog.setType(dto.getType());

        InstrumentLog savedLog = logRepository.save(newLog);
        return ResponseEntity.ok(savedLog);
    }

    // 2. Obtener todo el expediente médico de un instrumento
    @GetMapping("/instrument/{instrumentId}")
    public ResponseEntity<List<InstrumentLog>> getLogsByInstrument(@PathVariable UUID instrumentId) {
        // Asumiendo que crearás este método en tu Repositorio:
        // List<InstrumentLog> findByInstrumentIdOrderByEntryDateDesc(UUID instrumentId);
        List<InstrumentLog> logs = logRepository.findByInstrumentIdOrderByEntryDateDesc(instrumentId);
        return ResponseEntity.ok(logs);
    }
}