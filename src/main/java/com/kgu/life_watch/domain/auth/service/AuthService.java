package com.kgu.life_watch.domain.auth.service;

import com.kgu.life_watch.domain.auth.dto.SignUpRequest;
import com.kgu.life_watch.domain.auth.dto.SocialWorkerSignUpRequest;
import com.kgu.life_watch.domain.user.entity.User;
import com.kgu.life_watch.domain.user.repository.UserRepository;
import com.kgu.life_watch.global.exception.ErrorCode;
import com.kgu.life_watch.global.exception.LifelineException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest request) {
        // loginId 중복 검사
        if (userRepository.existsByLoginId(request.loginId())) {
            throw LifelineException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }

        // 회원 정보 저장
        User user = User.builder()
                .name(request.name())
                .loginId(request.loginId())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .rrn(request.rrn())
                .drn(request.drn())
                .socialWorkerId(request.socialWorkerId())
                .role(User.Role.USER)
                .build();

        userRepository.save(user);
    }
    public void signUpSocialWorker(SocialWorkerSignUpRequest request) {
        if (userRepository.existsByLoginId(request.loginId())) {
            throw LifelineException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }

        User socialWorker = User.builder()
                .name(request.name())
                .loginId(request.loginId())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .rrn(request.rrn())
                .role(User.Role.SOCIAL_WORKER)
                .build();

        userRepository.save(socialWorker);
    }
}
