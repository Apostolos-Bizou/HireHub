package com.hirehub.controller;

import com.hirehub.domain.Post;
import com.hirehub.domain.enums.PostType;
import com.hirehub.exception.ResourceNotFoundException;
import com.hirehub.repository.PostRepository;
import com.hirehub.repository.UserRepository;
import com.hirehub.security.UserPrincipal;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final PostRepository postRepo;
    private final UserRepository userRepo;

    @GetMapping
    public ResponseEntity<Page<Post>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(postRepo.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<Post> createPost(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody CreatePostRequest request) {
        Post post = Post.builder()
                .author(userRepo.getReferenceById(principal.getId()))
                .postType(request.getPostType() != null ? request.getPostType() : PostType.TEXT)
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(postRepo.save(post));
    }

    @Data
    public static class CreatePostRequest {
        @NotBlank private String content;
        private PostType postType;
        private String imageUrl;
    }
}
