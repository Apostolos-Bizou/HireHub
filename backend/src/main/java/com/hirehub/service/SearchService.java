package com.hirehub.service;

import com.hirehub.domain.SeafarerProfile;
import com.hirehub.dto.request.SearchCandidatesRequest;
import com.hirehub.dto.response.SearchResultResponse;
import com.hirehub.repository.SeafarerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SeafarerProfileRepository profileRepo;

    @Transactional(readOnly = true)
    public SearchResultResponse searchCandidates(SearchCandidatesRequest request) {
        Sort sort = switch (request.getSortBy()) {
            case "experience" -> Sort.by(Sort.Direction.DESC, "yearsOfExperience");
            case "availability" -> Sort.by(Sort.Direction.ASC, "availableFrom");
            default -> Sort.by(Sort.Direction.DESC, "profileCompleteness"); // fallback, AI score computed separately
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<SeafarerProfile> results = profileRepo.searchCandidates(
                request.getRank(),
                request.getVesselType(),
                request.getNationality(),
                request.getMinExperience(),
                request.getAvailableBefore(),
                request.isVerifiedOnly(),
                pageable);

        return SearchResultResponse.builder()
                .totalResults(results.getTotalElements())
                .page(results.getNumber())
                .size(results.getSize())
                .totalPages(results.getTotalPages())
                .candidates(results.getContent().stream()
                        .map(this::mapToCard)
                        .toList())
                .build();
    }

    private SearchResultResponse.CandidateCard mapToCard(SeafarerProfile p) {
        return SearchResultResponse.CandidateCard.builder()
                .profileId(p.getId().toString())
                .fullName(p.getFullName())
                .nationality(p.getNationality())
                .currentRank(p.getCurrentRank())
                .preferredVesselType(p.getPreferredVesselType() != null ?
                        p.getPreferredVesselType().name() : null)
                .yearsOfExperience(p.getYearsOfExperience())
                .verifiedStatus(p.getVerifiedStatus().name())
                .photoUrl(p.getPhotoUrl())
                .availableFrom(p.getAvailableFrom())
                .aiScore(BigDecimal.ZERO) // TODO: integrate with AI microservice
                .topCertificates(p.getCertificates().stream()
                        .limit(3)
                        .map(c -> c.getCertName())
                        .toList())
                .skills(p.getSkills().stream()
                        .limit(5)
                        .map(s -> s.getSkillName())
                        .toList())
                .build();
    }
}
