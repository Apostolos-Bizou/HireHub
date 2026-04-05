package com.hirehub.domain;

import com.hirehub.domain.enums.VesselType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vessels")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vessel extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "vessel_name", nullable = false, length = 200)
    private String vesselName;

    @Enumerated(EnumType.STRING)
    @Column(name = "vessel_type", length = 50)
    private VesselType vesselType;

    @Column(length = 50)
    private String dwt;

    @Column(name = "flag_state", length = 100)
    private String flagState;

    @Column(name = "imo_number", length = 20, unique = true)
    private String imoNumber;

    @Column(name = "year_built")
    private Integer yearBuilt;

    @Column(name = "crew_capacity")
    private Integer crewCapacity;
}
