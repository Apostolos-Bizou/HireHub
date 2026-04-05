package com.hirehub.controller;

import com.hirehub.domain.enums.CandidateStatus;
import com.hirehub.dto.request.AddCandidateRequest;
import com.hirehub.dto.request.CreateShortlistRequest;
import com.hirehub.dto.response.ShortlistResponse;
import com.hirehub.security.UserPrincipal;
import com.hirehub.service.ShortlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/shortlists")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SHIPOWNER', 'ADMIN')")
public class ShortlistController {

    private final ShortlistService shortlistService;

    @PostMapping
    public ResponseEntity<ShortlistResponse> createShortlist(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateShortlistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shortlistService.createShortlist(principal, request));
    }

    @GetMapping
    public ResponseEntity<Page<ShortlistResponse>> getMyShortlists(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(shortlistService.getMyShortlists(principal, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShortlistResponse> getShortlist(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(shortlistService.getShortlist(id, principal));
    }

    @PostMapping("/{id}/candidates")
    public ResponseEntity<ShortlistResponse> addCandidate(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AddCandidateRequest request) {
        return ResponseEntity.ok(shortlistService.addCandidate(id, principal, request));
    }

    @PatchMapping("/{shortlistId}/candidates/{candidateId}/status")
    public ResponseEntity<ShortlistResponse> updateCandidateStatus(
            @PathVariable UUID shortlistId,
            @PathVariable UUID candidateId,
            @RequestParam CandidateStatus status,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(
                shortlistService.updateCandidateStatus(shortlistId, candidateId, status, principal));
    }

    @PostMapping("/{id}/forward")
    public ResponseEntity<ShortlistResponse> forwardToAgent(
            @PathVariable UUID id,
            @RequestParam UUID agentId,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(shortlistService.forwardToAgent(id, agentId, principal));
    }
}
