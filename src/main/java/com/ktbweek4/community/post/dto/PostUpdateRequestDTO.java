package com.ktbweek4.community.post.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostUpdateRequestDTO {
    private String title;
    private String content;

    // 삭제할 이미지들 (기존 이미지의 ID)
    private List<Long> removeImageIds;

    // 대표 이미지 지정 (기존 이미지의 ID 혹은 새로 추가된 이미지의 ID)
    private Long primaryImageId;
}