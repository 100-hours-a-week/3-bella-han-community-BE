package com.ktbweek4.community.post.controller;

import com.ktbweek4.community.common.ApiResponse;
import com.ktbweek4.community.common.CommonCode;
import com.ktbweek4.community.post.dto.PostRequestDTO;
import com.ktbweek4.community.post.dto.PostResponseDTO;
import com.ktbweek4.community.post.dto.PostUpdateRequestDTO;
import com.ktbweek4.community.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostResponseDTO>> createPost(
            @RequestPart("post") PostRequestDTO postRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            HttpServletRequest request
    ) throws Exception {
        PostResponseDTO savedPost = postService.createPost(postRequestDTO, images, request);
        return ApiResponse.success(CommonCode.POST_CREATED, savedPost).toResponseEntity();
    }

    // 게시글 수정 (제목/내용 변경 + 이미지 추가/삭제 + 대표 이미지 설정)
    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostResponseDTO>> updatePost(
            @PathVariable Long postId,
            @RequestPart(value = "post", required = false) PostUpdateRequestDTO updateDTO,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            HttpServletRequest request
    ) throws Exception {
        if (updateDTO == null) updateDTO = new PostUpdateRequestDTO();
        PostResponseDTO updated = postService.updatePost(postId, updateDTO, newImages, request);
        return ApiResponse.success(CommonCode.POST_UPDATED, updated).toResponseEntity();
    }

    // 게시글 삭제

    // 게시글 목록 조회

    // 게시글 상세 조회
}
