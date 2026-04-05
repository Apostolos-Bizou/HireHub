package com.hirehub.repository;

import com.hirehub.domain.Consent;
import com.hirehub.domain.enums.ConsentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsentRepository extends JpaRepository<Consent, UUID> {
    List<Consent> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Consent> findByUserIdAndStatus(UUID userId, ConsentStatus status);
}
