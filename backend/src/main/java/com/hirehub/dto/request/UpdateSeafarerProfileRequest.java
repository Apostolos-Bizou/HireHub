package com.hirehub.dto.request;

import com.hirehub.domain.enums.VesselType;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateSeafarerProfileRequest {
    @Size(min = 2, max = 200)
    private String fullName;
    private String nationality;
    private LocalDate dateOfBirth;
    private String gender;
    private String currentRank;
    private VesselType preferredVesselType;
    private Integer yearsOfExperience;
    private String bio;
    private String phoneNumber;
    private String address;
    private String passportNumber;
    private String seamansBookNumber;
    private LocalDate availableFrom;
    private String desiredRank;
    private String contractPreference;
    private Integer salaryExpectationMin;
    private Integer salaryExpectationMax;
    private String salaryCurrency;
}
