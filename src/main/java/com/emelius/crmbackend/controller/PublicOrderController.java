package com.emelius.crmbackend.controller;

import com.emelius.crmbackend.dto.request.IntakeConditionDTO;
import com.emelius.crmbackend.dto.response.ServiceOrderResponseDTO;
import com.emelius.crmbackend.entity.tenant.Instrument;
import com.emelius.crmbackend.entity.tenant.InstrumentLog;
import com.emelius.crmbackend.entity.tenant.ServiceOrder;
import com.emelius.crmbackend.repository.tenant.CustomerRepository;
import com.emelius.crmbackend.repository.tenant.InstrumentLogRepository;
import com.emelius.crmbackend.repository.tenant.InstrumentRepository;
import com.emelius.crmbackend.repository.tenant.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/public/orders")
public class PublicOrderController {

    private ServiceOrderRepository orderRepository;
    private InstrumentRepository instrumentRepository;
    private CustomerRepository customerRepository;
    private InstrumentLogRepository instrumentLogRepository;

    public PublicOrderController(ServiceOrderRepository orderRepository, InstrumentRepository instrumentRepository, CustomerRepository customerRepository, InstrumentLogRepository instrumentLogRepository){
        this.orderRepository = orderRepository;
        this.instrumentRepository = instrumentRepository;
        this.customerRepository = customerRepository;
        this.instrumentLogRepository = instrumentLogRepository;
    }

    // 1. El cliente entra al link y cargamos los datos del presupuesto extra
    @GetMapping("/approval/{token}")
    public ResponseEntity<?> getOrderByToken(@PathVariable String token) {
        // Debes agregar este método a tu ServiceOrderRepository:
        // Optional<ServiceOrder> findByApprovalToken(String approvalToken);
        ServiceOrder order = orderRepository.findByApprovalToken(token)
                .orElseThrow(() -> new RuntimeException("Enlace inválido o expirado"));

        if(order.getInstrument() != null) {
            Instrument instrument = instrumentRepository.findById(order.getInstrument().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Instrumento no encontrado"));
            order.setInstrument(instrument);
            if(instrument.getCustomer() != null){
                instrument.setCustomer(customerRepository.findById(instrument.getCustomer().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado")));
            }

        }

        // Aquí idealmente mapeas a tu ServiceOrderResponseDTO para no mandar datos sensibles
        ServiceOrderResponseDTO dto = mapToResponse(order);
        return ResponseEntity.ok(dto);
    }

    // 2. El cliente marca la casilla y le da clic a "Aprobar Presupuesto"
    @PostMapping("/approval/{token}/accept")
    public ResponseEntity<?> acceptExtraBudget(@PathVariable String token) {
        ServiceOrder order = orderRepository.findByApprovalToken(token)
                .orElseThrow(() -> new RuntimeException("Enlace inválido o expirado"));

//        if (order.getTermsAcceptedAt() != null) {
//            return ResponseEntity.badRequest().body("Este presupuesto ya había sido aprobado anteriormente.");
//        }

        // Sellamos legalmente la fecha y hora de aceptación
        order.setTermsAcceptedAt(LocalDateTime.now());

        // Cambiamos el estatus para que en el CRM de Emedius se mueva automáticamente
        order.setStatus("APROBADO_POR_CLIENTE");

        // Aquí podrías destruir el token si quieres que sea de un solo uso:
        // order.setApprovalToken(null);
        // (Pero te recomiendo dejarlo por si el cliente quiere volver a entrar a ver su recibo)

        orderRepository.save(order);

        if(order.getInstrument() != null) {
            Instrument instrument = instrumentRepository.findById(order.getInstrument().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Instrumento no encontrado"));
            order.setInstrument(instrument);
        }

        assert order.getInstrument() != null;
        if(order.getInstrument().getCustomer() != null){
            order.getInstrument().setCustomer(customerRepository.findById(order.getInstrument().getCustomer().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado")));
        }

        InstrumentLog instrumentLog = new InstrumentLog();
        instrumentLog.setInstrument(order.getInstrument());
        instrumentLog.setType("Solicitud de presupuesto");
        instrumentLog.setNote("Presupuesto de $" + order.getExtraCost() + ", por el motivo de: " + order.getExtraWorkReason() + "\n\n Presupuesto aprobado por el cliente");
        instrumentLog.setAuthor(order.getInstrument().getCustomer() != null ? order.getInstrument().getCustomer().getName() : "Cliente");
        instrumentLog.setServiceOrderId(String.valueOf(order.getId()));
        instrumentLog.setEntryDate(LocalDateTime.now());

        instrumentLogRepository.save(instrumentLog);

        return ResponseEntity.ok("Presupuesto aprobado con éxito");
    }

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
            response.setInstrumentId(order.getInstrument().getId());
            response.setInstrumentBrand(order.getInstrument().getBrand());
            response.setInstrumentModel(order.getInstrument().getModel());

            if (order.getInstrument().getCustomer() != null) {
                response.setCustomerName(order.getInstrument().getCustomer().getName());
            }
        }

        if(order.getApprovalToken() != null) {
            response.setApprovalToken(order.getApprovalToken());
            response.setTermsAcceptedAt(order.getTermsAcceptedAt());
            response.setExtraCost(order.getExtraCost());
            response.setExtraWorkReason(order.getExtraWorkReason());
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