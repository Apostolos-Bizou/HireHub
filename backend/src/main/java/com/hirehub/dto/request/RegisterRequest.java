package com.hirehub.dto.request;

import com.hirehub.domain.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8, max = 100)
    private String password;

    @NotNull
    private UserRole role;

    @NotBlank @Size(min = 2, max = 200)
    private String fullName;

    // Optional for shipowner/agent registration
    private String companyName;
    private String dmwLicenseNumber;
}
