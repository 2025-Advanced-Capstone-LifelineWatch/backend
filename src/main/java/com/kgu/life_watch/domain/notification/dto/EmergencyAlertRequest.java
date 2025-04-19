package com.kgu.life_watch.domain.notification.dto;

public record EmergencyAlertRequest(Long elderlyId, String predictionLabel, String explanation) {}
