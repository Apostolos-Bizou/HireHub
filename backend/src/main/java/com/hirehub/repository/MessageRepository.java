package com.hirehub.repository;

import com.hirehub.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findByThreadIdOrderBySentAtDesc(UUID threadId, Pageable pageable);
}
