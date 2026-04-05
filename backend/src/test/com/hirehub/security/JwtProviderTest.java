package com.hirehub.security;

import com.hirehub.domain.User;
import com.hirehub.domain.enums.AccountStatus;
import com.hirehub.domain.enums.UserRole;
import org.junit.jupiter.api.*;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setup() {
        jwtProvider = new JwtProvider(
            "test_jwt_secret_key_for_unit_tests_must_be_at_least_256_bits_long_enough",
            3600000, 86400000);
    }

    private UserPrincipal createPrincipal(UserRole role) {
        User user = User.builder().email("test@test.com").passwordHash("hash")
                .role(role).accountStatus(AccountStatus.ACTIVE).build();
        user.setId(UUID.randomUUID());
        return new UserPrincipal(user);
    }

    @Test @DisplayName("Access token: generate, validate, extract userId")
    void accessToken() {
        UserPrincipal p = createPrincipal(UserRole.SEAFARER);
        String token = jwtProvider.generateAccessToken(p);
        assertNotNull(token);
        assertTrue(jwtProvider.validateToken(token));
        assertEquals(p.getId(), jwtProvider.getUserIdFromToken(token));
        assertEquals("access", jwtProvider.getTokenType(token));
    }

    @Test @DisplayName("Refresh token has type=refresh")
    void refreshToken() {
        String token = jwtProvider.generateRefreshToken(createPrincipal(UserRole.SHIPOWNER));
        assertTrue(jwtProvider.validateToken(token));
        assertEquals("refresh", jwtProvider.getTokenType(token));
    }

    @Test @DisplayName("Invalid tokens fail validation")
    void invalidTokens() {
        assertFalse(jwtProvider.validateToken("invalid.token"));
        assertFalse(jwtProvider.validateToken(""));
    }
}
