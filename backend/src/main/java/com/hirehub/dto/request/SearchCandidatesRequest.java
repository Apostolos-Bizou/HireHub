package com.hirehub.dto.request;

import com.hirehub.domain.enums.VesselType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchCandidatesRequest {
    private String rank;
    private VesselType vesselType;
    private String nationality;
    private Integer minExperience;
    private LocalDate availableBefore;
    private boolean verifiedOnly = false;
    private String sortBy = "aiScore"; // aiScore, experience, availability
    private int page = 0;
    private int size = 20;
}
