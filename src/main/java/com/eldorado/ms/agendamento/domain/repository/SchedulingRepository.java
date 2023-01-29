package com.eldorado.ms.agendamento.domain.repository;

import com.eldorado.ms.agendamento.domain.model.SchedulingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SchedulingRepository extends MongoRepository<SchedulingEntity, UUID> {
}
