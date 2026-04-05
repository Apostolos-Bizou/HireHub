package com.hirehub.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data @Builder
public class MessageThreadResponse {
    private String id;
    private String shortlistId;
    private Instant lastMessageAt;
    private List<ParticipantInfo> participants;
    private List<MessageItem> messages;
    private long unreadCount;

    @Data @Builder
    public static class ParticipantInfo {
        private String userId;
        private String name;
        private String role;
        private Instant lastReadAt;
    }

    @Data @Builder
    public static class MessageItem {
        private String id;
        private String senderId;
        private String senderName;
        private String body;
        private Instant sentAt;
        private boolean deleted;
    }
}
