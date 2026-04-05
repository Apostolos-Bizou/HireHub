package com.hirehub.repository;

import com.hirehub.domain.MessageThread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface MessageThreadRepository extends JpaRepository<MessageThread, UUID> {

    @Query("""
        SELECT t FROM MessageThread t
        JOIN t.participants p
        WHERE p.user.id = :userId
        ORDER BY t.lastMessageAt DESC
    """)
    Page<MessageThread> findThreadsByUserId(UUID userId, Pageable pageable);
}
