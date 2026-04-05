package com.hirehub.repository;

import com.hirehub.domain.Vessel;
import com.hirehub.domain.enums.VesselType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VesselRepository extends JpaRepository<Vessel, UUID> {
    List<Vessel> findByCompanyId(UUID companyId);
    Optional<Vessel> findByImoNumber(String imoNumber);
    List<Vessel> findByVesselType(VesselType vesselType);
}
