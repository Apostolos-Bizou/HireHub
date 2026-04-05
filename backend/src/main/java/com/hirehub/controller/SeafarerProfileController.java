package com.hirehub.controller;

import com.hirehub.dto.request.UpdateSeafarerProfileRequest;
import com.hirehub.dto.response.SeafarerProfileResponse;
import com.hirehub.security.UserPrincipal;
import com.hirehub.service.SeafarerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profiles/seafarer")
@RequiredArgsConstructor
public class SeafarerProfileController {

    private final SeafarerProfileService profileService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('SEAFARER')")
    public ResponseEntity<SeafarerProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(profileService.getMyProfile(principal));
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<SeafarerProfileResponse> getProfile(
            @PathVariable UUID profileId,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(profileService.getProfile(profileId, principal));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('SEAFARER')")
    public ResponseEntity<SeafarerProfileResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateSeafarerProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(principal, request));
    }
}
