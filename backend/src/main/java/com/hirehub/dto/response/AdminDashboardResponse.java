package com.hirehub.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
public class AdminDashboardResponse {

    private long totalUsers;
    private long totalSeafarers;
    private long totalShipowners;
    private long totalAgents;
    private long verifiedProfiles;
    private long pendingVerifications;
    private long totalShortlists;
    private long activeShortlists;
    private long totalMessages;
    private long recentSignups; // last 7 days

    @Data @Builder
    public static class UserRow {
        private String id;
        private String email;
        private String role;
        private String accountStatus;
        private String createdAt;
        private String lastActiveAt;
        private String profileName;
        private String companyName;
    }

    @Data @Builder
    public static class CompanyRow {
        private String id;
        private String companyName;
        private String companyType;
        private String country;
        private String dmwLicenseNumber;
        private boolean dmwLicenseVerified;
        private String verifiedStatus;
        private int fleetSize;
        private String createdAt;
    }
}
