package com.kgu.life_watch.domain.user.controller;

import com.kgu.life_watch.domain.user.dto.request.SignUpRequest;
import com.kgu.life_watch.domain.user.service.UserService;
import com.kgu.life_watch.global.dto.response.ApiResponse;
import com.kgu.life_watch.global.domain.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    // 회원가입 요청 처리
    @PostMapping("/signup")
    public ApiResponse<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        userService.signUp(request);
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }
}
