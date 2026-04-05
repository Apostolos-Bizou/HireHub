package com.hirehub.domain;

import com.hirehub.domain.enums.VerifiedStatus;
import com.hirehub.domain.enums.VesselType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seafarer_profiles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SeafarerProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(length = 100)
    private String nationality;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 10)
    private String gender;

    @Column(name = "current_rank", length = 100)
    private String currentRank;

    @Column(name = "preferred_vessel_type", length = 50)
    @Enumerated(EnumType.STRING)
    private VesselType preferredVesselType;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    // Contact
    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(length = 500)
    private String address;

    // Documents
    @Column(name = "passport_number", length = 50)
    private String passportNumber;

    @Column(name = "seamans_book_number", length = 50)
    private String seamansBookNumber;

    // Verification
    @Enumerated(EnumType.STRING)
    @Column(name = "verified_status", nullable = false, length = 20)
    @Builder.Default
    private VerifiedStatus verifiedStatus = VerifiedStatus.UNVERIFIED;

    @Column(name = "profile_completeness", nullable = false)
    @Builder.Default
    private Integer profileCompleteness = 0;

    // Availability
    @Column(name = "available_from")
    private LocalDate availableFrom;

    @Column(name = "desired_rank", length = 100)
    private String desiredRank;

    @Column(name = "contract_preference", length = 50)
    private String contractPreference;

    @Column(name = "salary_expectation_min")
    private Integer salaryExpectationMin;

    @Column(name = "salary_expectation_max")
    private Integer salaryExpectationMax;

    @Column(name = "salary_currency", length = 3)
    @Builder.Default
    private String salaryCurrency = "USD";

    // Relationships
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("startDate DESC")
    @Builder.Default
    private List<SeaService> seaServiceRecords = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Certificate> certificates = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SeafarerSkill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Education> educationRecords = new ArrayList<>();
}
