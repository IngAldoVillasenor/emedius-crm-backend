package com.emelius.crmbackend.service;

import com.emelius.crmbackend.dto.request.IntakeConditionDTO;
import com.emelius.crmbackend.dto.request.ServiceOrderRequestDTO;
import com.emelius.crmbackend.dto.response.ServiceOrderResponseDTO;
import com.emelius.crmbackend.entity.tenant.Instrument;
import com.emelius.crmbackend.entity.tenant.IntakeCondition;
import com.emelius.crmbackend.entity.tenant.ServiceOrder;
import com.emelius.crmbackend.repository.tenant.InstrumentRepository;
import com.emelius.crmbackend.repository.tenant.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final InstrumentRepository instrumentRepository;
    private final WhatsAppService whatsAppService;
    private final EmailService emailService;

    public ServiceOrderService(ServiceOrderRepository serviceOrderRepository,
                               InstrumentRepository instrumentRepository,
                               WhatsAppService whatsAppService,
                               EmailService emailService) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.instrumentRepository = instrumentRepository;
        this.whatsAppService = whatsAppService;
        this.emailService = emailService;
    }

    // La magia de @Transactional: si algo falla en la BD, revierte todo automáticamente
    @Transactional
    public ServiceOrderResponseDTO createServiceOrder(ServiceOrderRequestDTO dto) {

        // 1. Validar que el instrumento existe
        Instrument instrument = instrumentRepository.findById(dto.getInstrumentId())
                .orElseThrow(() -> new IllegalArgumentException("Instrumento no encontrado"));

        // 2. Crear la Orden de Servicio
        ServiceOrder order = new ServiceOrder();
        order.setInstrument(instrument);

        // ¡VITAL! Guardar el tipo de servicio (Pa'l Huesero, Rockstar, etc)
        order.setServiceType(dto.getServiceType());

        // Asignar las peticiones específicas (que llegaron como 'notes' desde Next.js)
        order.setSpecificRequests(dto.getSpecificRequests());

        // El status "RECIBIDO" y la fecha se asignan automáticamente por la Entidad

        // 3. Crear las Condiciones de Recepción
        IntakeCondition condition = new IntakeCondition();
        condition.setStringGauge(dto.getIntakeCondition().getStringGauge());
        condition.setAction1stFret(dto.getIntakeCondition().getAction1stFret());
        condition.setAction12thFret(dto.getIntakeCondition().getAction12thFret());
        condition.setPaintCondition(dto.getIntakeCondition().getPaintCondition());
        condition.setFretboardStatus(dto.getIntakeCondition().getFretboardStatus());
        condition.setHardwareStatus(dto.getIntakeCondition().getHardwareStatus());

        // 4. Ligar BIDIRECCIONALMENTE (Vital para que el Cascade guarde ambas tablas)
        condition.setServiceOrder(order);
        order.setIntakeCondition(condition);

        // 5. Guardar (Solo llamamos a save() en la orden, la condición se guarda sola)
        ServiceOrder savedOrder = serviceOrderRepository.save(order);

        // 6. Retornar respuesta usando nuestro método aplanador
        return mapToResponse(savedOrder);
    }

    @Transactional
    public ServiceOrderResponseDTO updateOrderStatus(UUID orderId, String newStatus) {
        ServiceOrder order = serviceOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        order.setStatus(newStatus.toUpperCase());
        ServiceOrder updatedOrder = serviceOrderRepository.save(order);

        // Si el nuevo estatus es "LISTO", disparamos el WhatsApp
        if ("LISTO".equalsIgnoreCase(newStatus)) {
            emailService.sendReadyNotification(
                    order.getInstrument().getCustomer().getEmail(),
                    order.getInstrument().getCustomer().getName(),
                    order.getInstrument().getBrand() + " " + order.getInstrument().getModel(),
                    order.getId().toString().split("-")[0].toUpperCase()
            );
        }

        // Retornar usando el método maestro
        return mapToResponse(updatedOrder);
    }

    @Transactional(readOnly = true)
    public ServiceOrderResponseDTO getServiceOrder(UUID id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        // Retornar usando el método maestro
        return mapToResponse(order);
    }

    @Transactional(readOnly = true)
    public List<ServiceOrderResponseDTO> getAllServiceOrders() {
        return serviceOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public ServiceOrderResponseDTO updateServiceOrder(UUID id, ServiceOrderRequestDTO dto) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        // 1. Actualizamos los datos básicos del servicio
        if (dto.getServiceType() != null) order.setServiceType(dto.getServiceType());
        if (dto.getSpecificRequests() != null) order.setSpecificRequests(dto.getSpecificRequests());

        // 2. Actualizamos la Hoja de Inspección (IntakeCondition)
        if (dto.getIntakeCondition() != null) {
            // Verificamos si ya tenía una hoja, si no, la creamos
            IntakeCondition condition = order.getIntakeCondition();
            if (condition == null) {
                condition = new IntakeCondition();
                condition.setServiceOrder(order);
                order.setIntakeCondition(condition);
            }

            // Asignamos los nuevos valores
            condition.setStringGauge(dto.getIntakeCondition().getStringGauge());
            condition.setAction1stFret(dto.getIntakeCondition().getAction1stFret());
            condition.setAction12thFret(dto.getIntakeCondition().getAction12thFret());
            condition.setPaintCondition(dto.getIntakeCondition().getPaintCondition());
            condition.setFretboardStatus(dto.getIntakeCondition().getFretboardStatus());
            condition.setHardwareStatus(dto.getIntakeCondition().getHardwareStatus());
        }

        ServiceOrder savedOrder = serviceOrderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    // El método maestro de aplanamiento
    private ServiceOrderResponseDTO mapToResponse(ServiceOrder order) {
        ServiceOrderResponseDTO response = new ServiceOrderResponseDTO();

        // 1. Datos básicos
        response.setId(order.getId());
        response.setServiceType(order.getServiceType());
        response.setStatus(order.getStatus());
        response.setNotes(order.getSpecificRequests());
        response.setEntryDate(order.getReceivedDate());

        // 2. Extracción de las relaciones (Aplanamiento)
        if (order.getInstrument() != null) {
            response.setInstrumentBrand(order.getInstrument().getBrand());
            response.setInstrumentModel(order.getInstrument().getModel());

            if (order.getInstrument().getCustomer() != null) {
                response.setCustomerName(order.getInstrument().getCustomer().getName());
            }
        }

        // 3. NUEVO: Mapeo de las Condiciones de Entrada
        if (order.getIntakeCondition() != null) {
            IntakeConditionDTO conditionDTO = new com.emelius.crmbackend.dto.request.IntakeConditionDTO();

            conditionDTO.setStringGauge(order.getIntakeCondition().getStringGauge());
            conditionDTO.setAction1stFret(order.getIntakeCondition().getAction1stFret());
            conditionDTO.setAction12thFret(order.getIntakeCondition().getAction12thFret());
            conditionDTO.setPaintCondition(order.getIntakeCondition().getPaintCondition());
            conditionDTO.setFretboardStatus(order.getIntakeCondition().getFretboardStatus());
            conditionDTO.setHardwareStatus(order.getIntakeCondition().getHardwareStatus());

            response.setIntakeCondition(conditionDTO);
        }

        return response;
    }
}