package com.emelius.crmbackend.service;

import com.emelius.crmbackend.dto.request.CustomerRequestDTO;
import com.emelius.crmbackend.dto.response.CustomerResponseDTO;
import com.emelius.crmbackend.entity.tenant.Customer;
import com.emelius.crmbackend.repository.tenant.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
        // 1. Mapear DTO a Entidad
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setWhatsappNumber(dto.getWhatsappNumber());
        customer.setEmail(dto.getEmail());

        // 2. Guardar en la base de datos (Hibernate usará el esquema correcto mágicamente)
        Customer savedCustomer = customerRepository.save(customer);

        // 3. Mapear Entidad a DTO de respuesta
        return mapToResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerResponseDTO updateCustomer(UUID id, CustomerRequestDTO dto) {
        // Buscamos al cliente por su ID
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        // Actualizamos los datos
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setWhatsappNumber(dto.getWhatsappNumber());

        // Guardamos y mapeamos a respuesta (asumiendo que tienes un método mapToResponse)
        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponse(savedCustomer);
    }

    // Método auxiliar para no repetir código
    private CustomerResponseDTO mapToResponse(Customer customer) {
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setWhatsappNumber(customer.getWhatsappNumber());
        response.setEmail(customer.getEmail());
        response.setCreatedAt(customer.getCreatedAt());
        return response;
    }
}