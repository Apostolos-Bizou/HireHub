package com.hirehub.repository;

import com.hirehub.domain.Shortlist;
import com.hirehub.domain.enums.ShortlistStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShortlistRepository extends JpaRepository<Shortlist, UUID> {
    Page<Shortlist> findByOwnerIdAndStatus(UUID ownerId, ShortlistStatus status, Pageable pageable);
    Page<Shortlist> findByOwnerId(UUID ownerId, Pageable pageable);
    Page<Shortlist> findByAgentId(UUID agentId, Pageable pageable);
}
