package com.hirehub.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data @Builder
public class SearchResultResponse {
    private long totalResults;
    private int page;
    private int size;
    private int totalPages;
    private List<CandidateCard> candidates;

    @Data @Builder
    public static class CandidateCard {
        private String profileId;
        private String fullName;
        private String nationality;
        private String currentRank;
        private String preferredVesselType;
        private Integer yearsOfExperience;
        private String verifiedStatus;
        private String photoUrl;
        private LocalDate availableFrom;
        private BigDecimal aiScore;
        private List<String> topCertificates;
        private List<String> skills;
    }
}
