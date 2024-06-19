package com.sidenow.freshgreenish.domain.likes.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKES_ID")
    private Long likesId;
    private Long userId;
    private Long productId;

    @Builder
    public Likes(Long likesId, Long userId, Long productId) {
        this.likesId = likesId;
        this.userId = userId;
        this.productId = productId;
    }
}
