package com.hirehub.service;

import com.hirehub.domain.Company;
import com.hirehub.domain.SeafarerProfile;
import com.hirehub.domain.User;
import com.hirehub.domain.enums.AccountStatus;
import com.hirehub.domain.enums.CompanyType;
import com.hirehub.domain.enums.UserRole;
import com.hirehub.dto.request.LoginRequest;
import com.hirehub.dto.request.RefreshTokenRequest;
import com.hirehub.dto.request.RegisterRequest;
import com.hirehub.dto.response.AuthResponse;
import com.hirehub.exception.BadRequestException;
import com.hirehub.exception.UnauthorizedException;
import com.hirehub.repository.CompanyRepository;
import com.hirehub.repository.SeafarerProfileRepository;
import com.hirehub.repository.UserRepository;
import com.hirehub.security.JwtProvider;
import com.hirehub.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SeafarerProfileRepository seafarerProfileRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        User user = User.builder()
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .accountStatus(AccountStatus.ACTIVE) // TODO: email verification flow
                .emailVerified(false)
                .build();
        user = userRepository.save(user);

        // Create role-specific profile
        switch (request.getRole()) {
            case SEAFARER -> {
                SeafarerProfile profile = SeafarerProfile.builder()
                        .user(user)
                        .fullName(request.getFullName())
                        .build();
                seafarerProfileRepository.save(profile);
            }
            case SHIPOWNER -> {
                Company company = Company.builder()
                        .user(user)
                        .companyName(request.getCompanyName() != null ?
                                request.getCompanyName() : request.getFullName())
                        .companyType(CompanyType.SHIPOWNER)
                        .build();
                companyRepository.save(company);
            }
            case MANNING_AGENT -> {
                Company company = Company.builder()
                        .user(user)
                        .companyName(request.getCompanyName() != null ?
                                request.getCompanyName() : request.getFullName())
                        .companyType(CompanyType.MANNING_AGENT)
                        .dmwLicenseNumber(request.getDmwLicenseNumber())
                        .build();
                companyRepository.save(company);
            }
            default -> throw new BadRequestException("Invalid role for registration");
        }

        return generateAuthResponse(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(Instant.now())) {
            throw new UnauthorizedException("Account temporarily locked. Try again later.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= 5) {
                user.setLockedUntil(Instant.now().plusSeconds(900)); // 15 min lockout
            }
            userRepository.save(user);
            throw new UnauthorizedException("Invalid email or password");
        }

        if (user.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new UnauthorizedException("Account is not active");
        }

        // Reset failed attempts and update last login
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        user.setLastLoginAt(Instant.now());
        user.setLastActiveAt(Instant.now());
        userRepository.save(user);

        return generateAuthResponse(user);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String token = request.getRefreshToken();

        if (!jwtProvider.validateToken(token) || !"refresh".equals(jwtProvider.getTokenType(token))) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        var userId = jwtProvider.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        return generateAuthResponse(user);
    }

    private AuthResponse generateAuthResponse(User user) {
        UserPrincipal principal = new UserPrincipal(user);
        String accessToken = jwtProvider.generateAccessToken(principal);
        String refreshToken = jwtProvider.generateRefreshToken(principal);

        String fullName = switch (user.getRole()) {
            case SEAFARER -> {
                var profile = seafarerProfileRepository.findByUserId(user.getId());
                yield profile.map(SeafarerProfile::getFullName).orElse(user.getEmail());
            }
            case SHIPOWNER, MANNING_AGENT -> {
                var company = companyRepository.findByUserId(user.getId());
                yield company.map(Company::getCompanyName).orElse(user.getEmail());
            }
            default -> user.getEmail();
        };

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600)
                .user(AuthResponse.UserSummary.builder()
                        .id(user.getId().toString())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .fullName(fullName)
                        .build())
                .build();
    }
}
