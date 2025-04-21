package com.kgu.life_watch.domain.notification.dto;

import java.time.LocalDateTime;

public record MedicineAlarmRequest(
    Long userId, String medicineName, LocalDateTime time, String medicineNote) {}
