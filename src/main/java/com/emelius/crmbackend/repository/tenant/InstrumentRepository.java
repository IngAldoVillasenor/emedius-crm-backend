package com.emelius.crmbackend.repository.tenant;

import com.emelius.crmbackend.entity.tenant.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, UUID> {
    // Spring Data JPA crea la consulta SQL automáticamente basándose en el nombre
    List<Instrument> findByCustomerId(UUID customerId);
}