package com.hirehub.domain;

import com.hirehub.domain.enums.PostType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Post extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", nullable = false, length = 20)
    @Builder.Default
    private PostType postType = PostType.TEXT;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "likes_count", nullable = false)
    @Builder.Default
    private int likesCount = 0;

    @Column(name = "comments_count", nullable = false)
    @Builder.Default
    private int commentsCount = 0;

    @Column(name = "shares_count", nullable = false)
    @Builder.Default
    private int sharesCount = 0;
}
