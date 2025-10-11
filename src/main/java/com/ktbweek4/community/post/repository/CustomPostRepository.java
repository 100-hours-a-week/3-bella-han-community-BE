// com.ktbweek4.community.post.repository.PostQueryRepository
package com.ktbweek4.community.post.repository;

import com.ktbweek4.community.post.dto.PostListItemDTO;

import java.util.List;

public interface CustomPostRepository {
    List<PostListItemDTO> fetchSliceByCursor(Long cursorPostId, int size);
}