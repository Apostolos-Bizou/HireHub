package com.hirehub.dto.response;

import com.hirehub.domain.enums.VerifiedStatus;
import com.hirehub.domain.enums.VesselType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data @Builder
public class SeafarerProfileResponse {
    private String id;
    private String userId;
    private String fullName;
    private String nationality;
    private LocalDate dateOfBirth;
    private String gender;
    private String currentRank;
    private VesselType preferredVesselType;
    private Integer yearsOfExperience;
    private String photoUrl;
    private String bio;
    private VerifiedStatus verifiedStatus;
    private Integer profileCompleteness;
    private LocalDate availableFrom;
    private String desiredRank;
    private String contractPreference;
    private Integer salaryExpectationMin;
    private Integer salaryExpectationMax;
    private String salaryCurrency;

    // Aggregated stats
    private long profileViews;
    private long shortlistCount;

    // Nested
    private List<SeaServiceItem> seaServiceRecords;
    private List<CertificateItem> certificates;
    private List<String> skills;
    private List<EducationItem> education;

    @Data @Builder
    public static class SeaServiceItem {
        private String id;
        private String vesselName;
        private String vesselType;
        private String dwt;
        private String flagState;
        private String rankHeld;
        private String companyName;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Data @Builder
    public static class CertificateItem {
        private String id;
        private String certType;
        private String certName;
        private String certNumber;
        private String issuingAuthority;
        private LocalDate issueDate;
        private LocalDate expiryDate;
        private String status;
        private String verifiedStatus;
    }

    @Data @Builder
    public static class EducationItem {
        private String id;
        private String institutionName;
        private String degree;
        private String fieldOfStudy;
        private Integer startYear;
        private Integer endYear;
    }
}
