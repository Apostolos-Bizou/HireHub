package com.hirehub.repository;

import com.hirehub.domain.ShortlistCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShortlistCandidateRepository extends JpaRepository<ShortlistCandidate, UUID> {
    List<ShortlistCandidate> findByShortlistId(UUID shortlistId);
    Optional<ShortlistCandidate> findByShortlistIdAndProfileId(UUID shortlistId, UUID profileId);
    boolean existsByShortlistIdAndProfileId(UUID shortlistId, UUID profileId);
    long countByProfileId(UUID profileId);
}
