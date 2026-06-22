package com.emelius.crmbackend.service;

import com.emelius.crmbackend.dto.request.InstrumentRequestDTO;
import com.emelius.crmbackend.dto.response.InstrumentResponseDTO;
import com.emelius.crmbackend.entity.tenant.Customer;
import com.emelius.crmbackend.entity.tenant.Instrument;
import com.emelius.crmbackend.repository.tenant.CustomerRepository;
import com.emelius.crmbackend.repository.tenant.InstrumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final CustomerRepository customerRepository;

    public InstrumentService(InstrumentRepository instrumentRepository, CustomerRepository customerRepository) {
        this.instrumentRepository = instrumentRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public InstrumentResponseDTO createInstrument(InstrumentRequestDTO dto) {
        // 1. Validar que el cliente exista
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + dto.getCustomerId()));

        // 2. Mapear Entidad
        Instrument instrument = new Instrument();
        instrument.setCustomer(customer); // Establecemos la relación
        instrument.setType(dto.getType());
        instrument.setBrand(dto.getBrand());
        instrument.setModel(dto.getModel());
        instrument.setSerialNumber(dto.getSerialNumber());

        // 3. Guardar
        Instrument savedInstrument = instrumentRepository.save(instrument);

        // 4. Retornar DTO
        return mapToResponse(savedInstrument);
    }

    @Transactional(readOnly = true)
    public List<InstrumentResponseDTO> getInstrumentsByCustomer(UUID customerId) {
        return instrumentRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InstrumentResponseDTO> getAllInstruments() {
        return instrumentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InstrumentResponseDTO updateInstrument(UUID id, InstrumentRequestDTO dto) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Instrumento no encontrado"));

        // Actualizamos los atributos básicos
        instrument.setBrand(dto.getBrand());
        instrument.setModel(dto.getModel());

        // NUEVO: Permitimos actualizar el tipo libremente
        if (dto.getType() != null && !dto.getType().isBlank()) {
            instrument.setType(dto.getType());
        }

        Instrument savedInstrument = instrumentRepository.save(instrument);
        return mapToResponse(savedInstrument);
    }

    private InstrumentResponseDTO mapToResponse(Instrument instrument) {
        InstrumentResponseDTO response = new InstrumentResponseDTO();
        response.setId(instrument.getId());
        response.setCustomerId(instrument.getCustomer().getId());
        response.setType(instrument.getType());
        response.setBrand(instrument.getBrand());
        response.setModel(instrument.getModel());
        response.setSerialNumber(instrument.getSerialNumber());
        response.setCustomerName(instrument.getCustomer().getName());
        response.setCreatedAt(instrument.getCreatedAt());
        return response;
    }
}