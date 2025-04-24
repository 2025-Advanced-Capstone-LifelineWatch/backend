package com.kgu.life_watch.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.auth.dto.*;
import com.kgu.life_watch.domain.auth.service.PasswordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthPasswordController {
  private final PasswordService passwordService;

  @PatchMapping("/password/change")
  public ResponseEntity<Void> changePassword(@RequestBody @Valid PasswordChangeRequest request) {
    passwordService.changePassword(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/find-id")
  public ResponseEntity<String> findLoginId(@RequestBody @Valid FindIdRequest request) {
    return ResponseEntity.ok(passwordService.findLoginId(request));
  }

  @PostMapping("/find-password")
  public ResponseEntity<Void> sendResetCode(@RequestBody @Valid FindPasswordRequest request) {
    passwordService.sendPasswordResetCode(request);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
    passwordService.resetPassword(request);
    return ResponseEntity.ok().build();
  }
}
