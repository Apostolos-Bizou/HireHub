package com.hirehub.repository;

import com.hirehub.domain.SeafarerProfile;
import com.hirehub.domain.enums.VerifiedStatus;
import com.hirehub.domain.enums.VesselType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface SeafarerProfileRepository extends JpaRepository<SeafarerProfile, UUID>,
        JpaSpecificationExecutor<SeafarerProfile> {

    Optional<SeafarerProfile> findByUserId(UUID userId);

    @Query("""
        SELECT p FROM SeafarerProfile p
        WHERE (:rank IS NULL OR LOWER(p.currentRank) = LOWER(:rank))
        AND (:vesselType IS NULL OR p.preferredVesselType = :vesselType)
        AND (:nationality IS NULL OR LOWER(p.nationality) = LOWER(:nationality))
        AND (:minExperience IS NULL OR p.yearsOfExperience >= :minExperience)
        AND (:availableBefore IS NULL OR p.availableFrom <= :availableBefore)
        AND (:verifiedOnly = false OR p.verifiedStatus = 'VERIFIED')
    """)
    Page<SeafarerProfile> searchCandidates(
            @Param("rank") String rank,
            @Param("vesselType") VesselType vesselType,
            @Param("nationality") String nationality,
            @Param("minExperience") Integer minExperience,
            @Param("availableBefore") LocalDate availableBefore,
            @Param("verifiedOnly") boolean verifiedOnly,
            Pageable pageable);
}
