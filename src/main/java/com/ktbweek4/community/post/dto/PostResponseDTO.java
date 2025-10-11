package com.ktbweek4.community.post.dto;

import com.ktbweek4.community.post.entity.PostEntity;
import com.ktbweek4.community.post.entity.PostImageEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
public class PostResponseDTO {
    private Long postId;
    private String title;
    private String content;
    private Long authorId;

    private List<ImageInfo> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    public static class ImageInfo {
        private Long imageId;
        private String url;
        private Byte orderIndex;
        private Boolean isPrimary;
    }

    public static PostResponseDTO of(PostEntity post) {
        List<ImageInfo> imageInfos = post.getPostImages().stream()
                .sorted(Comparator.comparing(PostImageEntity::getOrderIndex,
                        Comparator.nullsLast(Byte::compare)))
                .map(img -> ImageInfo.builder()
                        .imageId(img.getPostImageId())
                        .url(img.getPostImageUrl())
                        .orderIndex(img.getOrderIndex())
                        .isPrimary(img.getIsPrimary())
                        .build())
                .toList();

        return PostResponseDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthor().getUserId())
                .images(imageInfos)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}