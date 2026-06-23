package com.emelius.crmbackend.repository.tenant;

import com.emelius.crmbackend.entity.tenant.IntakeCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IntakeConditionRepository extends JpaRepository<IntakeCondition, UUID> {
}
