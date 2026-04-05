package com.hirehub.service;

import com.hirehub.domain.*;
import com.hirehub.domain.enums.*;
import com.hirehub.dto.response.AdminDashboardResponse;
import com.hirehub.exception.BadRequestException;
import com.hirehub.exception.ResourceNotFoundException;
import com.hirehub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepo;
    private final SeafarerProfileRepository profileRepo;
    private final CompanyRepository companyRepo;
    private final ShortlistRepository shortlistRepo;
    private final MessageRepository messageRepo;
    private final CertificateRepository certRepo;
    private final NotificationRepository notificationRepo;

    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboardStats() {
        Instant sevenDaysAgo = Instant.now().minus(7, ChronoUnit.DAYS);

        return AdminDashboardResponse.builder()
                .totalUsers(userRepo.count())
                .totalSeafarers(userRepo.countByRole(UserRole.SEAFARER))
                .totalShipowners(userRepo.countByRole(UserRole.SHIPOWNER))
                .totalAgents(userRepo.countByRole(UserRole.MANNING_AGENT))
                .verifiedProfiles(profileRepo.countByVerifiedStatus(VerifiedStatus.VERIFIED))
                .pendingVerifications(profileRepo.countByVerifiedStatus(VerifiedStatus.PENDING))
                .totalShortlists(shortlistRepo.count())
                .activeShortlists(shortlistRepo.countByStatus(ShortlistStatus.ACTIVE))
                .totalMessages(messageRepo.count())
                .recentSignups(userRepo.countByCreatedAtAfter(sevenDaysAgo))
                .build();
    }

    @Transactional(readOnly = true)
    public Page<AdminDashboardResponse.UserRow> getUsers(String role, String status,
                                                          int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<User> users;
        if (role != null && !role.isEmpty()) {
            users = userRepo.findByRole(UserRole.valueOf(role), pageable);
        } else {
            users = userRepo.findAll(pageable);
        }

        return users.map(u -> {
            String profileName = null;
            String companyName = null;

            if (u.getRole() == UserRole.SEAFARER) {
                profileName = profileRepo.findByUserId(u.getId())
                        .map(SeafarerProfile::getFullName).orElse(null);
            } else {
                companyName = companyRepo.findByUserId(u.getId())
                        .map(Company::getCompanyName).orElse(null);
            }

            return AdminDashboardResponse.UserRow.builder()
                    .id(u.getId().toString())
                    .email(u.getEmail())
                    .role(u.getRole().name())
                    .accountStatus(u.getAccountStatus().name())
                    .createdAt(u.getCreatedAt() != null ? u.getCreatedAt().toString() : null)
                    .lastActiveAt(u.getLastActiveAt() != null ? u.getLastActiveAt().toString() : null)
                    .profileName(profileName)
                    .companyName(companyName)
                    .build();
        });
    }

    @Transactional
    public void updateUserStatus(UUID userId, AccountStatus newStatus) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setAccountStatus(newStatus);
        userRepo.save(user);
    }

    @Transactional
    public void verifySeafarerProfile(UUID profileId) {
        SeafarerProfile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));
        profile.setVerifiedStatus(VerifiedStatus.VERIFIED);
        profileRepo.save(profile);
    }

    @Transactional
    public void rejectSeafarerProfile(UUID profileId) {
        SeafarerProfile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));
        profile.setVerifiedStatus(VerifiedStatus.REJECTED);
        profileRepo.save(profile);
    }

    @Transactional
    public void verifyCompanyDmwLicense(UUID companyId) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
        if (company.getCompanyType() != CompanyType.MANNING_AGENT) {
            throw new BadRequestException("DMW license verification applies only to manning agents");
        }
        company.setDmwLicenseVerified(true);
        company.setVerifiedStatus(VerifiedStatus.VERIFIED);
        companyRepo.save(company);
    }

    @Transactional(readOnly = true)
    public Page<AdminDashboardResponse.CompanyRow> getCompanies(String type, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Company> companies;
        if (type != null && !type.isEmpty()) {
            companies = companyRepo.findByCompanyType(CompanyType.valueOf(type), pageable);
        } else {
            companies = companyRepo.findAll(pageable);
        }

        return companies.map(c -> AdminDashboardResponse.CompanyRow.builder()
                .id(c.getId().toString())
                .companyName(c.getCompanyName())
                .companyType(c.getCompanyType().name())
                .country(c.getCountry())
                .dmwLicenseNumber(c.getDmwLicenseNumber())
                .dmwLicenseVerified(c.isDmwLicenseVerified())
                .verifiedStatus(c.getVerifiedStatus().name())
                .fleetSize(c.getFleetSize() != null ? c.getFleetSize() : 0)
                .createdAt(c.getCreatedAt() != null ? c.getCreatedAt().toString() : null)
                .build());
    }
}
