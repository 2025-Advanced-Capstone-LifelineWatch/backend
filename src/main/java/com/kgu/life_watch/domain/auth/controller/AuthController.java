package com.kgu.life_watch.domain.auth.controller;

import com.kgu.life_watch.domain.auth.dto.SignUpRequest;
import com.kgu.life_watch.domain.auth.dto.SocialWorkerSignUpRequest;
import com.kgu.life_watch.domain.auth.service.AuthService;
import com.kgu.life_watch.global.dto.response.ApiResponse;
import com.kgu.life_watch.global.domain.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        authService.signUp(request);
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }

    @PostMapping("/signup/social-worker")
    public ApiResponse<Void> signUpSocialWorker(@Valid @RequestBody SocialWorkerSignUpRequest request) {
        authService.signUpSocialWorker(request);
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }
}
