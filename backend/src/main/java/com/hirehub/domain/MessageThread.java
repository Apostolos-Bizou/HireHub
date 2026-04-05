package com.hirehub.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "threads")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MessageThread extends BaseEntity {

    @Column(name = "shortlist_id")
    private UUID shortlistId;

    @Column(name = "last_message_at", nullable = false)
    @Builder.Default
    private Instant lastMessageAt = Instant.now();

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ThreadParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sentAt ASC")
    @Builder.Default
    private List<Message> messages = new ArrayList<>();
}
