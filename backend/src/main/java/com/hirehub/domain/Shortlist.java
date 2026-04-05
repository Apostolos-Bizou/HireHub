package com.hirehub.domain;

import com.hirehub.domain.enums.ShortlistStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shortlists")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Shortlist extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(name = "vessel_context", length = 500)
    private String vesselContext;

    @Column(name = "agent_id")
    private UUID agentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ShortlistStatus status = ShortlistStatus.ACTIVE;

    @OneToMany(mappedBy = "shortlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ShortlistCandidate> candidates = new ArrayList<>();
}
