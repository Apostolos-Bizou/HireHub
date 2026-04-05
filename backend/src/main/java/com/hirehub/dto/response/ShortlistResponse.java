package com.hirehub.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data @Builder
public class ShortlistResponse {
    private String id;
    private String title;
    private String vesselContext;
    private String status;
    private String agentId;
    private Instant createdAt;
    private List<CandidateRow> candidates;

    @Data @Builder
    public static class CandidateRow {
        private String id;
        private String profileId;
        private String fullName;
        private String currentRank;
        private String nationality;
        private Integer yearsOfExperience;
        private String verifiedStatus;
        private BigDecimal aiScore;
        private String status;
        private Instant addedAt;
        private String notes;
    }
}
