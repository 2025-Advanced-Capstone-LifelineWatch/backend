package com.kgu.life_watch.domain.auth.dto;

import jakarta.validation.constraints.*;

public record SignUpRequest(
        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @NotBlank(message = "아이디는 필수입니다.")
        String loginId,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,}$",
                message = "비밀번호는 영문과 숫자를 혼합하여 10자 이상이어야 합니다."
        )
        String password,

        @NotBlank(message = "전화번호는 필수입니다.")
        String phoneNumber,

        @NotBlank(message = "주소는 필수입니다.")
        String address,

        @NotBlank(message = "주민등록번호는 필수입니다.")
        String rrn,

        @NotBlank(message = "장애인등록번호는 필수입니다.")
        String drn,

        @NotNull(message = "담당 사회복지사 ID는 필수입니다.")
        Long socialWorkerId
) {}
