package com.hirehub.domain;

import com.hirehub.domain.enums.VesselType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "sea_service")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SeaService extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private SeafarerProfile profile;

    @Column(name = "vessel_name", nullable = false, length = 200)
    private String vesselName;

    @Enumerated(EnumType.STRING)
    @Column(name = "vessel_type", length = 50)
    private VesselType vesselType;

    @Column(length = 50)
    private String dwt;

    @Column(name = "flag_state", length = 100)
    private String flagState;

    @Column(name = "imo_number", length = 20)
    private String imoNumber;

    @Column(name = "rank_held", nullable = false, length = 100)
    private String rankHeld;

    @Column(name = "company_name", length = 200)
    private String companyName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String duties;
}
