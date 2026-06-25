package com.emelius.crmbackend.repository.tenant;

import com.emelius.crmbackend.entity.tenant.InstrumentLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InstrumentLogRepository extends JpaRepository<InstrumentLog, UUID> {
    List<InstrumentLog> findByInstrumentIdOrderByEntryDateDesc(UUID instrumentId);
}
