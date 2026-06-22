package com.emelius.crmbackend.controller;

import com.emelius.crmbackend.dto.request.OrderStatusRequestDTO;
import com.emelius.crmbackend.dto.request.ServiceOrderRequestDTO;
import com.emelius.crmbackend.dto.response.ServiceOrderResponseDTO;
import com.emelius.crmbackend.service.ServiceOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-orders")
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    public ServiceOrderController(ServiceOrderService serviceOrderService) {
        this.serviceOrderService = serviceOrderService;
    }

    @PostMapping
    public ResponseEntity<ServiceOrderResponseDTO> createOrder(@Valid @RequestBody ServiceOrderRequestDTO request) {
        ServiceOrderResponseDTO response = serviceOrderService.createServiceOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ServiceOrderResponseDTO>> getAllOrders() {
        // Asumiendo que crearás este método en tu serviceOrderService
        List<ServiceOrderResponseDTO> responses = serviceOrderService.getAllServiceOrders();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderResponseDTO> getOrder(@PathVariable UUID id) {
        ServiceOrderResponseDTO response = serviceOrderService.getServiceOrder(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceOrderResponseDTO> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderStatusRequestDTO request) {

        ServiceOrderResponseDTO response = serviceOrderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderResponseDTO> updateServiceOrder(
            @PathVariable UUID id,
            @RequestBody ServiceOrderRequestDTO request) {
        ServiceOrderResponseDTO response = serviceOrderService.updateServiceOrder(id, request);
        return ResponseEntity.ok(response);
    }
}