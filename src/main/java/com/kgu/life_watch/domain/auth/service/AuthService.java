package com.kgu.life_watch.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.auth.dto.ElderlySignUpRequest;
import com.kgu.life_watch.domain.auth.dto.LoginRequest;
import com.kgu.life_watch.domain.auth.dto.SocialWorkerSignUpRequest;
import com.kgu.life_watch.domain.user.entity.ElderlyProfile;
import com.kgu.life_watch.domain.user.entity.SocialWorkerProfile;
import com.kgu.life_watch.domain.user.entity.User;
import com.kgu.life_watch.domain.user.repository.ElderlyProfileRepository;
import com.kgu.life_watch.domain.user.repository.SocialWorkerProfileRepository;
import com.kgu.life_watch.domain.user.repository.UserRepository;
import com.kgu.life_watch.global.exception.ErrorCode;
import com.kgu.life_watch.global.exception.LifelineException;
import com.kgu.life_watch.global.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final ElderlyProfileRepository elderlyProfileRepository;
  private final SocialWorkerProfileRepository socialWorkerProfileRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthSmsService authSmsService;

  public void signUpElderly(ElderlySignUpRequest request) {
    if (userRepository.existsByLoginId(request.loginId())) {
      throw LifelineException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
    }

    // SMS 인증번호 검증 로직
    if (!authSmsService.verifyCode(request.phoneNumber(), request.verificationCode())) {
      throw LifelineException.from(ErrorCode.SMS_VERIFICATION_FAILED);
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
            .role(User.Role.USER)
            .build();

    SocialWorkerProfile socialWorkerProfile =
        socialWorkerProfileRepository
            .findById(request.socialWorkerId())
            .orElseThrow(() -> LifelineException.from(ErrorCode.MEMBER_NOT_FOUND));

    ElderlyProfile elderlyProfile =
        ElderlyProfile.builder()
            .user(user)
            .drn(request.drn())
            .protectorContact(request.protectorContact())
            .socialWorkerProfile(socialWorkerProfile)
            .build();

    elderlyProfileRepository.save(elderlyProfile);
  }

  public void signUpSocialWorker(SocialWorkerSignUpRequest request) {
    if (userRepository.existsByLoginId(request.loginId())) {
      throw LifelineException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
    }

    // SMS 인증번호 검증 로직
    if (!authSmsService.verifyCode(request.phoneNumber(), request.verificationCode())) {
      throw LifelineException.from(ErrorCode.SMS_VERIFICATION_FAILED);
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

    SocialWorkerProfile profile = SocialWorkerProfile.builder().user(user).build();
    socialWorkerProfileRepository.save(profile);
  }

  public String login(LoginRequest request) {
    User user =
        userRepository
            .findByLoginId(request.loginId())
            .orElseThrow(() -> LifelineException.from(ErrorCode.INCORRECT_ACCOUNT));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw LifelineException.from(ErrorCode.INCORRECT_PASSWORD);
    }

    return jwtTokenProvider.generateToken(user);
  }
}
