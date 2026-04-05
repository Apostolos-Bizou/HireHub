package com.hirehub.service;

import com.hirehub.domain.*;
import com.hirehub.domain.enums.CandidateStatus;
import com.hirehub.domain.enums.ShortlistStatus;
import com.hirehub.dto.request.AddCandidateRequest;
import com.hirehub.dto.request.CreateShortlistRequest;
import com.hirehub.dto.response.ShortlistResponse;
import com.hirehub.exception.BadRequestException;
import com.hirehub.exception.ForbiddenException;
import com.hirehub.exception.ResourceNotFoundException;
import com.hirehub.repository.*;
import com.hirehub.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShortlistService {

    private final ShortlistRepository shortlistRepo;
    private final ShortlistCandidateRepository candidateRepo;
    private final SeafarerProfileRepository profileRepo;
    private final UserRepository userRepo;

    @Transactional
    public ShortlistResponse createShortlist(UserPrincipal principal, CreateShortlistRequest request) {
        Shortlist shortlist = Shortlist.builder()
                .owner(userRepo.getReferenceById(principal.getId()))
                .title(request.getTitle())
                .vesselContext(request.getVesselContext())
                .agentId(request.getAgentId())
                .build();
        shortlist = shortlistRepo.save(shortlist);
        return mapToResponse(shortlist);
    }

    @Transactional(readOnly = true)
    public Page<ShortlistResponse> getMyShortlists(UserPrincipal principal, int page, int size) {
        return shortlistRepo.findByOwnerId(principal.getId(),
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public ShortlistResponse getShortlist(UUID shortlistId, UserPrincipal principal) {
        Shortlist shortlist = shortlistRepo.findById(shortlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Shortlist", "id", shortlistId));
        verifyOwnership(shortlist, principal);
        return mapToResponse(shortlist);
    }

    @Transactional
    public ShortlistResponse addCandidate(UUID shortlistId, UserPrincipal principal,
                                          AddCandidateRequest request) {
        Shortlist shortlist = shortlistRepo.findById(shortlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Shortlist", "id", shortlistId));
        verifyOwnership(shortlist, principal);

        if (candidateRepo.existsByShortlistIdAndProfileId(shortlistId, request.getProfileId())) {
            throw new BadRequestException("Candidate already in shortlist");
        }

        SeafarerProfile profile = profileRepo.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", request.getProfileId()));

        ShortlistCandidate candidate = ShortlistCandidate.builder()
                .shortlist(shortlist)
                .profile(profile)
                .notes(request.getNotes())
                .build();
        candidateRepo.save(candidate);

        return mapToResponse(shortlistRepo.findById(shortlistId).get());
    }

    @Transactional
    public ShortlistResponse updateCandidateStatus(UUID shortlistId, UUID candidateId,
                                                    CandidateStatus status, UserPrincipal principal) {
        Shortlist shortlist = shortlistRepo.findById(shortlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Shortlist", "id", shortlistId));
        verifyOwnership(shortlist, principal);

        ShortlistCandidate candidate = candidateRepo.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", "id", candidateId));
        candidate.setStatus(status);
        candidateRepo.save(candidate);

        return mapToResponse(shortlistRepo.findById(shortlistId).get());
    }

    @Transactional
    public ShortlistResponse forwardToAgent(UUID shortlistId, UUID agentId, UserPrincipal principal) {
        Shortlist shortlist = shortlistRepo.findById(shortlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Shortlist", "id", shortlistId));
        verifyOwnership(shortlist, principal);

        shortlist.setAgentId(agentId);
        // Mark all shortlisted candidates as sent
        shortlist.getCandidates().stream()
                .filter(c -> c.getStatus() == CandidateStatus.SHORTLISTED)
                .forEach(c -> c.setStatus(CandidateStatus.SENT_TO_AGENT));
        shortlist = shortlistRepo.save(shortlist);

        return mapToResponse(shortlist);
    }

    private void verifyOwnership(Shortlist shortlist, UserPrincipal principal) {
        if (!shortlist.getOwner().getId().equals(principal.getId())) {
            throw new ForbiddenException("Not authorized to access this shortlist");
        }
    }

    private ShortlistResponse mapToResponse(Shortlist s) {
        return ShortlistResponse.builder()
                .id(s.getId().toString())
                .title(s.getTitle())
                .vesselContext(s.getVesselContext())
                .status(s.getStatus().name())
                .agentId(s.getAgentId() != null ? s.getAgentId().toString() : null)
                .createdAt(s.getCreatedAt())
                .candidates(s.getCandidates().stream()
                        .map(c -> ShortlistResponse.CandidateRow.builder()
                                .id(c.getId().toString())
                                .profileId(c.getProfile().getId().toString())
                                .fullName(c.getProfile().getFullName())
                                .currentRank(c.getProfile().getCurrentRank())
                                .nationality(c.getProfile().getNationality())
                                .yearsOfExperience(c.getProfile().getYearsOfExperience())
                                .verifiedStatus(c.getProfile().getVerifiedStatus().name())
                                .aiScore(c.getAiScore())
                                .status(c.getStatus().name())
                                .addedAt(c.getAddedAt())
                                .notes(c.getNotes())
                                .build())
                        .toList())
                .build();
    }
}
