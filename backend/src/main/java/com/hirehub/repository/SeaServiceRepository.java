package com.hirehub.repository;

import com.hirehub.domain.SeaService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeaServiceRepository extends JpaRepository<SeaService, UUID> {
    List<SeaService> findByProfileIdOrderByStartDateDesc(UUID profileId);
}
