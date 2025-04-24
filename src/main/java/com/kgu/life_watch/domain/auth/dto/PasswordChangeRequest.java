package com.kgu.life_watch.domain.auth.dto;

public record PasswordChangeRequest(String loginId, String currentPassword, String newPassword) {}
