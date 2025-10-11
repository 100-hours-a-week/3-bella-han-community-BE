package com.ktbweek4.community.auth;

import com.ktbweek4.community.common.ApiResponse;
import com.ktbweek4.community.common.CommonCode;
import com.ktbweek4.community.user.dto.LoginRequestDTO;
import com.ktbweek4.community.user.dto.UserResponseDTO;
import com.ktbweek4.community.user.entity.User;
import com.ktbweek4.community.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // 로그인 : 세션 생성(세션아이디 발급) + 세션에 사용자 저장
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(
            @RequestBody LoginRequestDTO request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        User user = userService.findByEmailOrThrow(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ApiResponse.<UserResponseDTO>error(CommonCode.USER_INVALID_PASSWORD).toResponseEntity();
        }

        HttpSession session = httpRequest.getSession(true); // 세션 생성

        // 세션 고정 공격 방지: 로그인 직후 세션 아이디 회전
        httpRequest.changeSessionId();


        session.setAttribute("LOGIN_USER", user);           // 사용자 보관

        // Spring Security 인증 객체 생성
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        var authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), // principal (또는 UserDetails를 넣어도 됨)
                null,            // credentials는 보통 null
                authorities
        );

        // 5) 🔥 SecurityContext에 저장
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // 6) 🔥 SecurityContext를 세션에 저장 (아주 중요)
        new HttpSessionSecurityContextRepository().saveContext(context, httpRequest, httpResponse);

        return ApiResponse.success(CommonCode.LOGIN_SUCCESS, UserResponseDTO.of(user)).toResponseEntity(); // 200
    }

    // 로그아웃: 세션 만료
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ApiResponse.<Void>success(CommonCode.LOGOUT_SUCCESS).toResponseEntity(); // 204
    }


}
