package com.hirehub.domain;

import com.hirehub.domain.enums.CertificateStatus;
import com.hirehub.domain.enums.CertificateType;
import com.hirehub.domain.enums.VerifiedStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "certificates")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Certificate extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private SeafarerProfile profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "cert_type", nullable = false, length = 30)
    private CertificateType certType;

    @Column(name = "cert_name", nullable = false, length = 300)
    private String certName;

    @Column(name = "cert_number", length = 100)
    private String certNumber;

    @Column(name = "issuing_authority", length = 200)
    private String issuingAuthority;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    @Builder.Default
    private CertificateStatus status = CertificateStatus.PENDING_VERIFICATION;

    @Enumerated(EnumType.STRING)
    @Column(name = "verified_status", nullable = false, length = 20)
    @Builder.Default
    private VerifiedStatus verifiedStatus = VerifiedStatus.UNVERIFIED;

    @Column(name = "document_url", length = 500)
    private String documentUrl;

    @Column(name = "verification_notes", columnDefinition = "TEXT")
    private String verificationNotes;
}
