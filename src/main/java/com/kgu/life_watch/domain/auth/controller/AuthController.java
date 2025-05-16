package com.kgu.life_watch.domain.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.auth.dto.request.ElderlySignUpRequest;
import com.kgu.life_watch.domain.auth.dto.request.FcmTokenUpdateRequest;
import com.kgu.life_watch.domain.auth.dto.request.LoginRequest;
import com.kgu.life_watch.domain.auth.dto.request.SocialWorkerSignUpRequest;
import com.kgu.life_watch.domain.auth.dto.response.LoginResponse;
import com.kgu.life_watch.domain.auth.service.AuthService;
import com.kgu.life_watch.global.domain.SuccessCode;
import com.kgu.life_watch.global.dto.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "AuthController", description = "인증 관련 API")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  @Operation(summary = "노인 회원가입 API", description = "노인 회원가입 API입니다.")
  public ApiResponse<Void> signUp(@Valid @RequestBody ElderlySignUpRequest request) {
    authService.signUpElderly(request);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }

  @PostMapping("/signup/social-worker")
  @Operation(summary = "사회복지사 회원가입 API", description = "사회복지사 회원가입 API입니다.")
  public ApiResponse<Void> signUpSocialWorker(
      @Valid @RequestBody SocialWorkerSignUpRequest request) {
    authService.signUpSocialWorker(request);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }

  @PostMapping("/login")
  @Operation(summary = "로그인 API", description = "로그인 API입니다.")
  public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginResponse response = authService.login(request);
    return new ApiResponse<>(response);
  }

  @PatchMapping("/fcm-token")
  @Operation(summary = "FCM 토큰 갱신 API", description = "사용자의 FCM 토큰을 최신값으로 갱신합니다.")
  public ApiResponse<Void> updateFcmToken(
      @AuthenticationPrincipal Long userId, @Valid @RequestBody FcmTokenUpdateRequest request) {
    authService.updateFcmToken(userId, request.fcmToken());
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }
}
