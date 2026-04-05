package com.hirehub.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seafarer_skills")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SeafarerSkill extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private SeafarerProfile profile;

    @Column(name = "skill_name", nullable = false, length = 100)
    private String skillName;

    @Column(name = "endorsement_count", nullable = false)
    @Builder.Default
    private int endorsementCount = 0;
}
