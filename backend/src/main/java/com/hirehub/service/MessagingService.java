package com.hirehub.service;

import com.hirehub.domain.*;
import com.hirehub.dto.request.SendMessageRequest;
import com.hirehub.dto.response.MessageThreadResponse;
import com.hirehub.exception.ForbiddenException;
import com.hirehub.exception.ResourceNotFoundException;
import com.hirehub.repository.*;
import com.hirehub.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final MessageThreadRepository threadRepo;
    private final MessageRepository messageRepo;
    private final UserRepository userRepo;

    @Transactional(readOnly = true)
    public Page<MessageThreadResponse> getThreads(UserPrincipal principal, int page, int size) {
        return threadRepo.findThreadsByUserId(principal.getId(), PageRequest.of(page, size))
                .map(t -> mapToResponse(t, principal.getId()));
    }

    @Transactional
    public MessageThreadResponse getThread(UUID threadId, UserPrincipal principal) {
        MessageThread thread = threadRepo.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Thread", "id", threadId));
        verifyParticipant(thread, principal.getId());

        // Mark as read
        thread.getParticipants().stream()
                .filter(p -> p.getUser().getId().equals(principal.getId()))
                .findFirst()
                .ifPresent(p -> p.setLastReadAt(Instant.now()));
        threadRepo.save(thread);

        return mapToResponse(thread, principal.getId());
    }

    @Transactional
    public MessageThreadResponse sendMessage(UUID threadId, UserPrincipal principal,
                                              SendMessageRequest request) {
        MessageThread thread = threadRepo.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Thread", "id", threadId));
        verifyParticipant(thread, principal.getId());

        Message message = Message.builder()
                .thread(thread)
                .sender(userRepo.getReferenceById(principal.getId()))
                .body(request.getBody())
                .build();
        thread.getMessages().add(message);
        thread.setLastMessageAt(Instant.now());
        threadRepo.save(thread);

        return mapToResponse(thread, principal.getId());
    }

    @Transactional
    public MessageThreadResponse startThread(UserPrincipal principal, UUID recipientId,
                                              UUID shortlistId, SendMessageRequest request) {
        MessageThread thread = MessageThread.builder()
                .shortlistId(shortlistId)
                .build();
        thread = threadRepo.save(thread);

        // Add participants
        ThreadParticipant sender = ThreadParticipant.builder()
                .thread(thread).user(userRepo.getReferenceById(principal.getId())).build();
        ThreadParticipant recipient = ThreadParticipant.builder()
                .thread(thread).user(userRepo.getReferenceById(recipientId)).build();
        thread.getParticipants().add(sender);
        thread.getParticipants().add(recipient);

        // Add first message
        Message message = Message.builder()
                .thread(thread)
                .sender(userRepo.getReferenceById(principal.getId()))
                .body(request.getBody())
                .build();
        thread.getMessages().add(message);
        thread.setLastMessageAt(Instant.now());

        thread = threadRepo.save(thread);
        return mapToResponse(thread, principal.getId());
    }

    private void verifyParticipant(MessageThread thread, UUID userId) {
        boolean isParticipant = thread.getParticipants().stream()
                .anyMatch(p -> p.getUser().getId().equals(userId));
        if (!isParticipant) {
            throw new ForbiddenException("Not a participant of this thread");
        }
    }

    private MessageThreadResponse mapToResponse(MessageThread t, UUID currentUserId) {
        return MessageThreadResponse.builder()
                .id(t.getId().toString())
                .shortlistId(t.getShortlistId() != null ? t.getShortlistId().toString() : null)
                .lastMessageAt(t.getLastMessageAt())
                .participants(t.getParticipants().stream()
                        .map(p -> MessageThreadResponse.ParticipantInfo.builder()
                                .userId(p.getUser().getId().toString())
                                .name(p.getUser().getEmail()) // TODO: resolve name from profile
                                .role(p.getUser().getRole().name())
                                .lastReadAt(p.getLastReadAt())
                                .build())
                        .toList())
                .messages(t.getMessages().stream()
                        .map(m -> MessageThreadResponse.MessageItem.builder()
                                .id(m.getId().toString())
                                .senderId(m.getSender().getId().toString())
                                .senderName(m.getSender().getEmail())
                                .body(m.isDeleted() ? "[Message deleted]" : m.getBody())
                                .sentAt(m.getSentAt())
                                .isDeleted(m.isDeleted())
                                .build())
                        .toList())
                .build();
    }
}
