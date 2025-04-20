package com.kgu.life_watch.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.user.dto.UserProfileResponse;
import com.kgu.life_watch.domain.user.entity.ElderlyProfile;
import com.kgu.life_watch.domain.user.entity.SocialWorkerProfile;
import com.kgu.life_watch.domain.user.entity.User;
import com.kgu.life_watch.domain.user.repository.ElderlyProfileRepository;
import com.kgu.life_watch.domain.user.repository.SocialWorkerProfileRepository;
import com.kgu.life_watch.global.exception.ErrorCode;
import com.kgu.life_watch.global.exception.LifelineException;

@RequiredArgsConstructor
@Service
public class UserService {
  private final ElderlyProfileRepository elderlyProfileRepository;
  private final SocialWorkerProfileRepository socialWorkerProfileRepository;

  @Transactional
  public void assignElderly(Long elderlyId, Long socialWorkerId) {
    ElderlyProfile elderly =
        elderlyProfileRepository
            .findById(elderlyId)
            .orElseThrow(() -> LifelineException.from(ErrorCode.MEMBER_NOT_FOUND));
    SocialWorkerProfile socialWorker =
        socialWorkerProfileRepository
            .findById(socialWorkerId)
            .orElseThrow(() -> LifelineException.from(ErrorCode.MEMBER_NOT_FOUND));

    // 연관관계 편의 메서드를 통해 노인을 사회복지사에게 할당
    socialWorker.addElderly(elderly);
  }

  @Transactional
  public void unassignElderly(Long elderlyId, Long socialWorkerId) {
    ElderlyProfile elderly =
        elderlyProfileRepository
            .findById(elderlyId)
            .orElseThrow(() -> LifelineException.from(ErrorCode.MEMBER_NOT_FOUND));
    SocialWorkerProfile socialWorker =
        socialWorkerProfileRepository
            .findById(socialWorkerId)
            .orElseThrow(() -> LifelineException.from(ErrorCode.MEMBER_NOT_FOUND));

    socialWorker.getAssignedSeniors().remove(elderly);
    // 노인의 사회복지사 연관관계 해제
    elderly.setSocialWorkerProfile(null);
  }

  @Transactional(readOnly = true)
  public UserProfileResponse getProfile(User user) {
    return UserProfileResponse.toDto(user);
  }
}
