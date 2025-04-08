package com.kgu.life_watch.domain.auth.controller;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.auth.dto.ElderlySignUpRequest;
import com.kgu.life_watch.domain.auth.dto.LoginRequest;
import com.kgu.life_watch.domain.auth.dto.SocialWorkerSignUpRequest;
import com.kgu.life_watch.domain.auth.service.AuthService;
import com.kgu.life_watch.global.domain.SuccessCode;
import com.kgu.life_watch.global.dto.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ApiResponse<Void> signUp(@Valid @RequestBody ElderlySignUpRequest request) {
    authService.signUpElderly(request);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }

  @PostMapping("/signup/social-worker")
  public ApiResponse<Void> signUpSocialWorker(
      @Valid @RequestBody SocialWorkerSignUpRequest request) {
    authService.signUpSocialWorker(request);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }

  @PostMapping("/login")
  public ApiResponse<String> login(@Valid @RequestBody LoginRequest request) {
    String token = authService.login(request);
    return new ApiResponse<>(token);
  }
}
