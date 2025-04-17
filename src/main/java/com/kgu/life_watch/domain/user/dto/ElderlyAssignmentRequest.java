package com.kgu.life_watch.domain.user.dto;

import jakarta.validation.constraints.NotNull;

public record ElderlyAssignmentRequest(@NotNull Long elderlyId, @NotNull Long socialWorkerId) {}
