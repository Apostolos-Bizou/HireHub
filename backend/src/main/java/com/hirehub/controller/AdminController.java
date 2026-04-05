package com.hirehub.controller;

import com.hirehub.domain.enums.AccountStatus;
import com.hirehub.dto.response.AdminDashboardResponse;
import com.hirehub.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    // ── Dashboard ────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // ── Users ────────────────────────────────────────────────────
    @GetMapping("/users")
    public ResponseEntity<Page<AdminDashboardResponse.UserRow>> getUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminService.getUsers(role, status, page, size));
    }

    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<Map<String, String>> updateUserStatus(
            @PathVariable UUID userId,
            @RequestBody Map<String, String> body) {
        AccountStatus newStatus = AccountStatus.valueOf(body.get("status"));
        adminService.updateUserStatus(userId, newStatus);
        return ResponseEntity.ok(Map.of("message", "User status updated to " + newStatus));
    }

    // ── Verification ─────────────────────────────────────────────
    @PatchMapping("/profiles/{profileId}/verify")
    public ResponseEntity<Map<String, String>> verifyProfile(@PathVariable UUID profileId) {
        adminService.verifySeafarerProfile(profileId);
        return ResponseEntity.ok(Map.of("message", "Profile verified"));
    }

    @PatchMapping("/profiles/{profileId}/reject")
    public ResponseEntity<Map<String, String>> rejectProfile(@PathVariable UUID profileId) {
        adminService.rejectSeafarerProfile(profileId);
        return ResponseEntity.ok(Map.of("message", "Profile rejected"));
    }

    @PatchMapping("/companies/{companyId}/verify-dmw")
    public ResponseEntity<Map<String, String>> verifyDmwLicense(@PathVariable UUID companyId) {
        adminService.verifyCompanyDmwLicense(companyId);
        return ResponseEntity.ok(Map.of("message", "DMW license verified"));
    }

    // ── Companies ────────────────────────────────────────────────
    @GetMapping("/companies")
    public ResponseEntity<Page<AdminDashboardResponse.CompanyRow>> getCompanies(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminService.getCompanies(type, page, size));
    }
}
