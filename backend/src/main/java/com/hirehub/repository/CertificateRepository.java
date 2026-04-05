package com.hirehub.repository;

import com.hirehub.domain.Certificate;
import com.hirehub.domain.enums.CertificateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    List<Certificate> findByProfileId(UUID profileId);

    @Query("SELECT c FROM Certificate c WHERE c.expiryDate BETWEEN :start AND :end AND c.status = :status")
    List<Certificate> findExpiringSoon(LocalDate start, LocalDate end, CertificateStatus status);
}
