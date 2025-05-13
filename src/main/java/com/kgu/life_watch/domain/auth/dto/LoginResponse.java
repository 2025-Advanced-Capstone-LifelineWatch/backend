package com.kgu.life_watch.domain.auth.dto;

import java.time.LocalDate;

public record LoginResponse(
    String name,
    LocalDate birthDate,
    String protectorName,
    String protectorContact,
    String socialWorkerName,
    String socialWorkerPhone,
    Long userId,
    boolean isSocialWorker) {}
