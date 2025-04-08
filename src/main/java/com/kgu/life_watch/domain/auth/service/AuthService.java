package com.kgu.life_watch.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kgu.life_watch.domain.auth.dto.ElderlySignUpRequest;
import com.kgu.life_watch.domain.auth.dto.LoginRequest;
import com.kgu.life_watch.domain.auth.dto.SocialWorkerSignUpRequest;
import com.kgu.life_watch.domain.user.entity.ElderlyProfile;
import com.kgu.life_watch.domain.user.entity.SocialWorkerProfile;
import com.kgu.life_watch.domain.user.entity.User;
import com.kgu.life_watch.domain.user.repository.UserRepository;
import com.kgu.life_watch.global.exception.ErrorCode;
import com.kgu.life_watch.global.exception.LifelineException;
import com.kgu.life_watch.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  public void signUpElderly(ElderlySignUpRequest request) {
    // 로그인 ID 중복 확인
    if (userRepository.existsByLoginId(request.loginId())) {
      throw LifelineException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
    }

    // 일반 사용자(User) 엔티티 생성
    User user =
        User.builder()
            .name(request.name())
            .loginId(request.loginId())
            .email(request.email())
            .password(passwordEncoder.encode(request.password())) // 비밀번호 암호화
            .phoneNumber(request.phoneNumber())
            .address(request.address())
            .rrn(request.rrn())
            .birthDate(request.birthDate())
            .gender(request.gender())
            .role(User.Role.USER)
            .build();

    // 노인 프로필 생성 및 연결
    ElderlyProfile elderlyProfile =
        ElderlyProfile.builder()
            .user(user)
            .drn(request.drn())
            .socialWorkerId(request.socialWorkerId())
            .protectorContact(request.protectorContact())
            .build();

    user.setElderlyProfile(elderlyProfile); // 양방향 연관관계 설정
    userRepository.save(user);
  }

  public void signUpSocialWorker(SocialWorkerSignUpRequest request) {
    // 사회복지사 회원가입
    if (userRepository.existsByLoginId(request.loginId())) {
      throw LifelineException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
    }

    User user =
        User.builder()
            .name(request.name())
            .loginId(request.loginId())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .phoneNumber(request.phoneNumber())
            .address(request.address())
            .rrn(request.rrn())
            .birthDate(request.birthDate())
            .gender(request.gender())
            .role(User.Role.SOCIAL_WORKER)
            .build();

    SocialWorkerProfile workerProfile =
        SocialWorkerProfile.builder().user(user).assignedElderId(request.assignedElderId()).build();

    user.setSocialWorkerProfile(workerProfile);
    userRepository.save(user);
  }

  public String login(LoginRequest request) {
    // 로그인 ID 기반 사용자 조회
    User user =
        userRepository
            .findByLoginId(request.loginId())
            .orElseThrow(() -> LifelineException.from(ErrorCode.INCORRECT_ACCOUNT));

    // 비밀번호 비교
    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw LifelineException.from(ErrorCode.INCORRECT_PASSWORD);
    }

    // JWT 토큰 생성 및 반환
    return jwtTokenProvider.generateToken(user);
  }
}
