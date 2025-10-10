package com.ktbweek4.community.user.controller;

import com.ktbweek4.community.common.ApiResponse;
import com.ktbweek4.community.common.CommonCode;
import com.ktbweek4.community.user.dto.UserRequestDTO;
import com.ktbweek4.community.user.dto.UserResponseDTO;
import com.ktbweek4.community.user.entity.User;
import com.ktbweek4.community.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDTO>> signup(@RequestBody UserRequestDTO request) {
        UserResponseDTO saved = userService.create(request);
        return ApiResponse.success(CommonCode.USER_CREATED, saved).toResponseEntity(); // 201 + 바디
    }

    // 세션에서 로그인 사용자 가져오기
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDTO>> me(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return ApiResponse.<UserResponseDTO>error(CommonCode.UNAUTHORIZED).toResponseEntity();
        }
        User user = (User) session.getAttribute("LOGIN_USER");
        if (user == null) {
            return ApiResponse.<UserResponseDTO>error(CommonCode.UNAUTHORIZED).toResponseEntity();
        }
        return ApiResponse.success(CommonCode.SUCCESS, UserResponseDTO.of(user)).toResponseEntity();
    }
}
