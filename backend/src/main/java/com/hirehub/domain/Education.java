package com.hirehub.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "education")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Education extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private SeafarerProfile profile;

    @Column(name = "institution_name", nullable = false, length = 300)
    private String institutionName;

    @Column(length = 200)
    private String degree;

    @Column(name = "field_of_study", length = 200)
    private String fieldOfStudy;

    @Column(name = "start_year")
    private Integer startYear;

    @Column(name = "end_year")
    private Integer endYear;
}
