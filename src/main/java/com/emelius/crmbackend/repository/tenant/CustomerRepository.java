package com.emelius.crmbackend.repository.tenant;

import com.emelius.crmbackend.entity.tenant.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    // Si a futuro quieres buscar clientes por WhatsApp, solo agregas esto:
    // Optional<Customer> findByWhatsappNumber(String whatsappNumber);
}