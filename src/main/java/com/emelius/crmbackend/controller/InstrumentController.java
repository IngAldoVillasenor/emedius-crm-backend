package com.emelius.crmbackend.controller;

import com.emelius.crmbackend.dto.request.InstrumentRequestDTO;
import com.emelius.crmbackend.dto.response.InstrumentResponseDTO;
import com.emelius.crmbackend.service.InstrumentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @PostMapping
    public ResponseEntity<InstrumentResponseDTO> createInstrument(@Valid @RequestBody InstrumentRequestDTO request) {
        InstrumentResponseDTO response = instrumentService.createInstrument(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InstrumentResponseDTO>> getAllInstruments() {
        // Asumimos que vas a crear este método en tu servicio
        List<InstrumentResponseDTO> responses = instrumentService.getAllInstruments();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<InstrumentResponseDTO>> getInstrumentsByCustomer(@PathVariable UUID customerId) {
        List<InstrumentResponseDTO> responses = instrumentService.getInstrumentsByCustomer(customerId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstrumentResponseDTO> updateInstrument(
            @PathVariable UUID id,
            @RequestBody InstrumentRequestDTO request) {
        InstrumentResponseDTO response = instrumentService.updateInstrument(id, request);
        return ResponseEntity.ok(response);
    }
}