package com.hirehub.service;

import com.hirehub.domain.*;
import com.hirehub.dto.request.UpdateSeafarerProfileRequest;
import com.hirehub.dto.response.SeafarerProfileResponse;
import com.hirehub.exception.ForbiddenException;
import com.hirehub.exception.ResourceNotFoundException;
import com.hirehub.repository.*;
import com.hirehub.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeafarerProfileService {

    private final SeafarerProfileRepository profileRepo;
    private final ProfileViewRepository profileViewRepo;
    private final ShortlistCandidateRepository shortlistCandidateRepo;
    private final UserRepository userRepo;

    @Transactional(readOnly = true)
    public SeafarerProfileResponse getProfile(UUID profileId, UserPrincipal viewer) {
        SeafarerProfile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));

        // Record profile view if viewer is not the owner
        if (viewer != null && !profile.getUser().getId().equals(viewer.getId())) {
            ProfileView view = ProfileView.builder()
                    .profile(profile)
                    .viewer(userRepo.getReferenceById(viewer.getId()))
                    .viewedAt(Instant.now())
                    .build();
            profileViewRepo.save(view);
        }

        return mapToResponse(profile);
    }

    @Transactional(readOnly = true)
    public SeafarerProfileResponse getMyProfile(UserPrincipal principal) {
        SeafarerProfile profile = profileRepo.findByUserId(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "userId", principal.getId()));
        return mapToResponse(profile);
    }

    @Transactional
    public SeafarerProfileResponse updateProfile(UserPrincipal principal, UpdateSeafarerProfileRequest request) {
        SeafarerProfile profile = profileRepo.findByUserId(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "userId", principal.getId()));

        if (!profile.getUser().getId().equals(principal.getId())) {
            throw new ForbiddenException("Cannot update another user's profile");
        }

        // Update fields if provided
        if (request.getFullName() != null) profile.setFullName(request.getFullName());
        if (request.getNationality() != null) profile.setNationality(request.getNationality());
        if (request.getDateOfBirth() != null) profile.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) profile.setGender(request.getGender());
        if (request.getCurrentRank() != null) profile.setCurrentRank(request.getCurrentRank());
        if (request.getPreferredVesselType() != null) profile.setPreferredVesselType(request.getPreferredVesselType());
        if (request.getYearsOfExperience() != null) profile.setYearsOfExperience(request.getYearsOfExperience());
        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getPhoneNumber() != null) profile.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) profile.setAddress(request.getAddress());
        if (request.getPassportNumber() != null) profile.setPassportNumber(request.getPassportNumber());
        if (request.getSeamansBookNumber() != null) profile.setSeamansBookNumber(request.getSeamansBookNumber());
        if (request.getAvailableFrom() != null) profile.setAvailableFrom(request.getAvailableFrom());
        if (request.getDesiredRank() != null) profile.setDesiredRank(request.getDesiredRank());
        if (request.getContractPreference() != null) profile.setContractPreference(request.getContractPreference());
        if (request.getSalaryExpectationMin() != null) profile.setSalaryExpectationMin(request.getSalaryExpectationMin());
        if (request.getSalaryExpectationMax() != null) profile.setSalaryExpectationMax(request.getSalaryExpectationMax());
        if (request.getSalaryCurrency() != null) profile.setSalaryCurrency(request.getSalaryCurrency());

        // Recalculate completeness
        profile.setProfileCompleteness(calculateCompleteness(profile));

        profile = profileRepo.save(profile);
        return mapToResponse(profile);
    }

    private int calculateCompleteness(SeafarerProfile p) {
        int score = 0;
        int total = 12;
        if (p.getFullName() != null) score++;
        if (p.getNationality() != null) score++;
        if (p.getDateOfBirth() != null) score++;
        if (p.getCurrentRank() != null) score++;
        if (p.getPreferredVesselType() != null) score++;
        if (p.getYearsOfExperience() != null) score++;
        if (p.getPhotoUrl() != null) score++;
        if (p.getBio() != null && !p.getBio().isBlank()) score++;
        if (p.getAvailableFrom() != null) score++;
        if (!p.getSeaServiceRecords().isEmpty()) score++;
        if (!p.getCertificates().isEmpty()) score++;
        if (!p.getSkills().isEmpty()) score++;
        return (int) Math.round((double) score / total * 100);
    }

    private SeafarerProfileResponse mapToResponse(SeafarerProfile p) {
        return SeafarerProfileResponse.builder()
                .id(p.getId().toString())
                .userId(p.getUser().getId().toString())
                .fullName(p.getFullName())
                .nationality(p.getNationality())
                .dateOfBirth(p.getDateOfBirth())
                .gender(p.getGender())
                .currentRank(p.getCurrentRank())
                .preferredVesselType(p.getPreferredVesselType())
                .yearsOfExperience(p.getYearsOfExperience())
                .photoUrl(p.getPhotoUrl())
                .bio(p.getBio())
                .verifiedStatus(p.getVerifiedStatus())
                .profileCompleteness(p.getProfileCompleteness())
                .availableFrom(p.getAvailableFrom())
                .desiredRank(p.getDesiredRank())
                .contractPreference(p.getContractPreference())
                .salaryExpectationMin(p.getSalaryExpectationMin())
                .salaryExpectationMax(p.getSalaryExpectationMax())
                .salaryCurrency(p.getSalaryCurrency())
                .profileViews(profileViewRepo.countByProfileId(p.getId()))
                .shortlistCount(shortlistCandidateRepo.countByProfileId(p.getId()))
                .seaServiceRecords(p.getSeaServiceRecords().stream()
                        .map(ss -> SeafarerProfileResponse.SeaServiceItem.builder()
                                .id(ss.getId().toString())
                                .vesselName(ss.getVesselName())
                                .vesselType(ss.getVesselType() != null ? ss.getVesselType().name() : null)
                                .dwt(ss.getDwt())
                                .flagState(ss.getFlagState())
                                .rankHeld(ss.getRankHeld())
                                .companyName(ss.getCompanyName())
                                .startDate(ss.getStartDate())
                                .endDate(ss.getEndDate())
                                .build())
                        .toList())
                .certificates(p.getCertificates().stream()
                        .map(c -> SeafarerProfileResponse.CertificateItem.builder()
                                .id(c.getId().toString())
                                .certType(c.getCertType().name())
                                .certName(c.getCertName())
                                .certNumber(c.getCertNumber())
                                .issuingAuthority(c.getIssuingAuthority())
                                .issueDate(c.getIssueDate())
                                .expiryDate(c.getExpiryDate())
                                .status(c.getStatus().name())
                                .verifiedStatus(c.getVerifiedStatus().name())
                                .build())
                        .toList())
                .skills(p.getSkills().stream()
                        .map(SeafarerSkill::getSkillName)
                        .toList())
                .education(p.getEducationRecords().stream()
                        .map(e -> SeafarerProfileResponse.EducationItem.builder()
                                .id(e.getId().toString())
                                .institutionName(e.getInstitutionName())
                                .degree(e.getDegree())
                                .fieldOfStudy(e.getFieldOfStudy())
                                .startYear(e.getStartYear())
                                .endYear(e.getEndYear())
                                .build())
                        .toList())
                .build();
    }
}
