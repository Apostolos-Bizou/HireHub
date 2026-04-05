package com.hirehub.domain;

import com.hirehub.domain.enums.CandidateStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "shortlist_candidates")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShortlistCandidate extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shortlist_id", nullable = false)
    private Shortlist shortlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private SeafarerProfile profile;

    @Column(name = "ai_score", precision = 5, scale = 2)
    private BigDecimal aiScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CandidateStatus status = CandidateStatus.SHORTLISTED;

    @Column(name = "added_at", nullable = false)
    @Builder.Default
    private Instant addedAt = Instant.now();

    @Column(columnDefinition = "TEXT")
    private String notes;
}
