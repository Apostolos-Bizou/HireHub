package com.hirehub.repository;

import com.hirehub.domain.ProfileView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.UUID;

public interface ProfileViewRepository extends JpaRepository<ProfileView, UUID> {
    long countByProfileId(UUID profileId);

    @Query("SELECT COUNT(pv) FROM ProfileView pv WHERE pv.profile.id = :profileId AND pv.viewedAt >= :since")
    long countRecentViews(UUID profileId, Instant since);
}
