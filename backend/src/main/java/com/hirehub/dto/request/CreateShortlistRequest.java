package com.hirehub.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateShortlistRequest {
    @NotBlank
    private String title;
    private String vesselContext;
    private UUID agentId;
}
