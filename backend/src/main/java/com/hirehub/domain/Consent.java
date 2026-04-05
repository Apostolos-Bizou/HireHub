package com.hirehub.domain;

import com.hirehub.domain.enums.ConsentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "consents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Consent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "consent_type", nullable = false, length = 100)
    private String consentType;

    @Column(name = "consent_version", nullable = false, length = 20)
    private String consentVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private ConsentStatus status = ConsentStatus.GIVEN;

    @Column(name = "given_at", nullable = false)
    @Builder.Default
    private Instant givenAt = Instant.now();

    @Column(name = "withdrawn_at")
    private Instant withdrawnAt;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;
}
