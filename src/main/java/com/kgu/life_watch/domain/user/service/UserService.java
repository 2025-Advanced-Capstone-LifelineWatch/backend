package com.kgu.life_watch.domain.user.service;

import com.kgu.life_watch.domain.user.dto.request.SignUpRequest;
import com.kgu.life_watch.domain.user.entity.User;
import com.kgu.life_watch.domain.user.repository.UserRepository;
import com.kgu.life_watch.global.exception.ErrorCode;
import com.kgu.life_watch.global.exception.LifelineException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void signUp(SignUpRequest request) {
        // loginId 중복 검사
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw LifelineException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }

        // 회원 정보 저장 (비밀번호는 암호화 후 저장)
        User user = User.builder()
                .name(request.getName())
                .loginId(request.getLoginId())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .rrn(request.getRrn())
                .drn(request.getDrn())
                .socialWorkerId(request.getSocialWorkerId())
                .role(User.Role.USER)
                .build();

        userRepository.save(user);
    }
}
