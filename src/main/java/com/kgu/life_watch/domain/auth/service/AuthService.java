package com.kgu.life_watch.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.auth.dto.ElderlySignUpRequest;
import com.kgu.life_watch.domain.auth.dto.LoginRequest;
import com.kgu.life_watch.domain.auth.dto.LoginResponse;
import com.kgu.life_watch.domain.auth.dto.SocialWorkerSignUpRequest;
import com.kgu.life_watch.domain.chat.service.ChatRoomService;
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
  private final ChatRoomService chatRoomService;

  public void signUpElderly(ElderlySignUpRequest request, String fcmToken) {
    if (userRepository.existsByLoginId(request.loginId())) {
      throw LifelineException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
    }

    Long socialWorkerId;
    try {
      socialWorkerId = Long.valueOf(request.socialWorkerId()); // 명시적 변환
    } catch (NumberFormatException e) {
      throw LifelineException.from(ErrorCode.INVALID_REQUEST);
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
            .fcmToken(fcmToken)
            .role(User.Role.USER)
            .build();

    SocialWorkerProfile socialWorkerProfile =
        socialWorkerProfileRepository
            .findById(socialWorkerId)
            .orElseThrow(() -> LifelineException.from(ErrorCode.MEMBER_NOT_FOUND));

    ElderlyProfile elderlyProfile =
        ElderlyProfile.builder()
            .user(user)
            .drn(request.drn())
            .protectorContact(request.protectorContact())
            .protectorName(request.protectorName())
            .socialWorkerProfile(socialWorkerProfile)
            .build();

    elderlyProfileRepository.save(elderlyProfile);
    elderlyProfileRepository.flush();

    // 담당 사회복지사와 채팅방 생성
    chatRoomService.createChatRoom(user, Long.valueOf(request.socialWorkerId()));
  }

  public void signUpSocialWorker(SocialWorkerSignUpRequest request, String fcmToken) {
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
            .fcmToken(fcmToken)
            .role(User.Role.SOCIAL_WORKER)
            .build();

    SocialWorkerProfile profile = SocialWorkerProfile.builder().user(user).build();
    socialWorkerProfileRepository.save(profile);
  }

  // string 대신 dto 반환해부리기
  @Transactional(readOnly = true)
  public LoginResponse login(LoginRequest request) {
    User user =
        userRepository
            .findByLoginId(request.loginId())
            .orElseThrow(() -> LifelineException.from(ErrorCode.INCORRECT_ACCOUNT));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw LifelineException.from(ErrorCode.INCORRECT_PASSWORD);
    }

    String jwt = jwtTokenProvider.generateToken(user);

    if (user.getRole() == User.Role.USER && user.getElderlyProfile() != null) {
      ElderlyProfile elderly = user.getElderlyProfile();
      SocialWorkerProfile worker = elderly.getSocialWorkerProfile();

      return new LoginResponse(
          user.getName(),
          jwt,
          user.getBirthDate(),
          elderly.getProtectorName(),
          elderly.getProtectorContact(),
          worker.getUser().getName(),
          worker.getUser().getPhoneNumber(),
          user.getId(),
          false);
    }

    if (user.getRole() == User.Role.SOCIAL_WORKER && user.getSocialWorkerProfile() != null) {
      return new LoginResponse(
          user.getName(), jwt, user.getBirthDate(), null, null, null, null, user.getId(), true);
    }

    throw LifelineException.from(ErrorCode.INCORRECT_ACCOUNT);
  }
}
