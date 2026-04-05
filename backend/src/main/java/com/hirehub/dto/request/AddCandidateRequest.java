package com.hirehub.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddCandidateRequest {
    @NotNull
    private UUID profileId;
    private String notes;
}
