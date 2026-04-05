package com.hirehub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @Builder @AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;
    private UserSummary user;

    @Data @Builder @AllArgsConstructor
    public static class UserSummary {
        private String id;
        private String email;
        private String role;
        private String fullName;
    }
}
