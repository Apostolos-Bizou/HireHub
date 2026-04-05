package com.hirehub.repository;

import com.hirehub.domain.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EducationRepository extends JpaRepository<Education, UUID> {
    List<Education> findByProfileIdOrderByEndYearDesc(UUID profileId);
}
