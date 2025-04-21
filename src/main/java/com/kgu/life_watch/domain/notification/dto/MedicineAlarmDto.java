package com.kgu.life_watch.domain.notification.dto;

import java.time.LocalTime;

public record MedicineAlarmDto(Long elderlyId, String medicineName, LocalTime time) {}
