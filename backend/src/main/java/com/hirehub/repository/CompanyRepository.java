package com.hirehub.repository;

import com.hirehub.domain.Company;
import com.hirehub.domain.enums.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByUserId(UUID userId);
    List<Company> findByCompanyType(CompanyType type);
    Optional<Company> findByDmwLicenseNumber(String licenseNumber);
}
