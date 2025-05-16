package com.kgu.life_watch.domain.auth.dto;

import lombok.Builder;

@Builder
public record FindLoginIdResponse(String loginId) {}
