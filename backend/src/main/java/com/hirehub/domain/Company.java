package com.hirehub.domain;

import com.hirehub.domain.enums.CompanyType;
import com.hirehub.domain.enums.VerifiedStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Company extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "company_name", nullable = false, length = 300)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type", nullable = false, length = 20)
    private CompanyType companyType;

    @Column(length = 100)
    private String country;

    @Column(length = 200)
    private String city;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String website;

    // Manning Agent specific
    @Column(name = "dmw_license_number", length = 50)
    private String dmwLicenseNumber;

    @Column(name = "dmw_license_verified", nullable = false)
    @Builder.Default
    private boolean dmwLicenseVerified = false;

    @Column(name = "principals_served")
    private Integer principalsServed;

    // Shipowner specific
    @Column(name = "fleet_size")
    private Integer fleetSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "verified_status", nullable = false, length = 20)
    @Builder.Default
    private VerifiedStatus verifiedStatus = VerifiedStatus.UNVERIFIED;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Vessel> vessels = new ArrayList<>();
}
