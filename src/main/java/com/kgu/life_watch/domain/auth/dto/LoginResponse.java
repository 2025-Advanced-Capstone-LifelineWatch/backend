package com.kgu.life_watch.domain.auth.dto;

import java.time.LocalDate;

public record LoginResponse(
    String socialWorkerName,
    String socialWorkerPhoneNumber,
    LocalDate birthDate,
    String protectorName,
    String protectorContact,
    Long elderlyId) {}
