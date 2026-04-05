package com.hirehub.repository;

import com.hirehub.domain.AiScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AiScoreRepository extends JpaRepository<AiScore, UUID> {
    Optional<AiScore> findByProfileIdAndSearchCriteriaHash(UUID profileId, String hash);
}
