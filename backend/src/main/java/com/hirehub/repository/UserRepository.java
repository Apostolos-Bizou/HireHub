package com.hirehub.repository;

import com.hirehub.domain.User;
import com.hirehub.domain.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByRole(UserRole role);
    long countByCreatedAtAfter(Instant after);
    Page<User> findByRole(UserRole role, Pageable pageable);
}
