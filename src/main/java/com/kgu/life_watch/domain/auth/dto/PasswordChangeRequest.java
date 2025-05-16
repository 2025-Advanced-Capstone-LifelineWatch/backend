package com.kgu.life_watch.domain.auth.dto;

public record PasswordChangeRequest(String currentPassword, String newPassword) {}
