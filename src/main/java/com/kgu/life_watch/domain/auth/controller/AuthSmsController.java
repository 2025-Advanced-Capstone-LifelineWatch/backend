package com.kgu.life_watch.domain.auth.controller;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.auth.dto.SmsVerificationRequest;
import com.kgu.life_watch.domain.auth.service.AuthSmsService;
import com.kgu.life_watch.global.domain.SuccessCode;
import com.kgu.life_watch.global.dto.response.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthSmsController {

  private final AuthSmsService smsService;

  // 인증 메세지 발송
  @GetMapping("/sms")
  public ApiResponse<String> sendSms(@RequestParam String phone) {
    smsService.sendAuthenticationCode(phone);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }

  // 인증
  @PostMapping("/sms/verify")
  public ApiResponse<Boolean> verifyCode(@RequestBody SmsVerificationRequest request) {
    boolean isValid = smsService.verifyCode(request.phoneNumber(), request.verificationCode());
    return new ApiResponse<>(isValid);
  }
}
