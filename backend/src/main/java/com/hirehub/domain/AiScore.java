package com.hirehub.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "ai_scores",
       uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "search_criteria_hash"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AiScore extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private SeafarerProfile profile;

    @Column(name = "search_criteria_hash", nullable = false, length = 64)
    private String searchCriteriaHash;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "criteria_breakdown", columnDefinition = "jsonb")
    private Map<String, Object> criteriaBreakdown;

    @Column(name = "computed_at", nullable = false)
    @Builder.Default
    private Instant computedAt = Instant.now();

    @Column(name = "expires_at")
    private Instant expiresAt;
}
