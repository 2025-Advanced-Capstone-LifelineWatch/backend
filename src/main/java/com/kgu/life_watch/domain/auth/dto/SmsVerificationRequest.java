package com.kgu.life_watch.domain.auth.dto;

public record SmsVerificationRequest(String phoneNumber, String verificationCode) {}
