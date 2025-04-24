package com.kgu.life_watch.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.auth.dto.*;
import com.kgu.life_watch.domain.auth.service.PasswordService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "AuthPasswordController", description = "비밀번호/아이디 찾기 및 변경 관련 API")
public class AuthPasswordController {

  private final PasswordService passwordService;

  @Operation(summary = "비밀번호 변경 API", description = "기존 비밀번호를 확인하고 새 비밀번호로 변경합니다.")
  @PatchMapping("/password/change")
  public ResponseEntity<Void> changePassword(@RequestBody @Valid PasswordChangeRequest request) {
    passwordService.changePassword(request);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "아이디 찾기 API", description = "이름과 전화번호를 통해 로그인 ID(아이디)를 조회합니다.")
  @PostMapping("/find-id")
  public ResponseEntity<String> findLoginId(@RequestBody @Valid FindIdRequest request) {
    return ResponseEntity.ok(passwordService.findLoginId(request));
  }

  @Operation(summary = "비밀번호 찾기 인증번호 전송 API", description = "로그인 ID와 전화번호를 통해 인증번호를 전송합니다.")
  @PostMapping("/find-password")
  public ResponseEntity<Void> sendResetCode(@RequestBody @Valid FindPasswordRequest request) {
    passwordService.sendPasswordResetCode(request);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "비밀번호 재설정 API", description = "인증번호를 검증하고 새 비밀번호로 변경합니다.")
  @PatchMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
    passwordService.resetPassword(request);
    return ResponseEntity.ok().build();
  }
}
