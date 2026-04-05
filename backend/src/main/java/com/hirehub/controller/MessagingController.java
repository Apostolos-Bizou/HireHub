package com.hirehub.controller;

import com.hirehub.dto.request.SendMessageRequest;
import com.hirehub.dto.response.MessageThreadResponse;
import com.hirehub.security.UserPrincipal;
import com.hirehub.service.MessagingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessagingController {

    private final MessagingService messagingService;

    @GetMapping("/threads")
    public ResponseEntity<Page<MessageThreadResponse>> getThreads(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(messagingService.getThreads(principal, page, size));
    }

    @GetMapping("/threads/{threadId}")
    public ResponseEntity<MessageThreadResponse> getThread(
            @PathVariable UUID threadId,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(messagingService.getThread(threadId, principal));
    }

    @PostMapping("/threads/{threadId}")
    public ResponseEntity<MessageThreadResponse> sendMessage(
            @PathVariable UUID threadId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody SendMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messagingService.sendMessage(threadId, principal, request));
    }

    @PostMapping("/new")
    public ResponseEntity<MessageThreadResponse> startThread(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam UUID recipientId,
            @RequestParam(required = false) UUID shortlistId,
            @Valid @RequestBody SendMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messagingService.startThread(principal, recipientId, shortlistId, request));
    }
}
