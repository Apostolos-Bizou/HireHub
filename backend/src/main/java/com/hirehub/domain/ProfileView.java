package com.hirehub.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "profile_views")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProfileView extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private SeafarerProfile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viewer_id", nullable = false)
    private User viewer;

    @Column(name = "viewed_at", nullable = false)
    @Builder.Default
    private Instant viewedAt = Instant.now();
}
