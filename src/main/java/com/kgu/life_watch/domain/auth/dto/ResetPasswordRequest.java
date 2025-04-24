package com.kgu.life_watch.domain.auth.dto;

public record ResetPasswordRequest(String loginId, String newPassword, String verificationCode) {}
