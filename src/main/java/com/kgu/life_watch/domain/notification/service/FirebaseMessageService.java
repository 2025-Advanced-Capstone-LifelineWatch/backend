package com.kgu.life_watch.domain.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.kgu.life_watch.domain.notification.entity.MedicineAlarm;
import com.kgu.life_watch.domain.user.entity.ElderlyProfile;
import com.kgu.life_watch.domain.user.entity.SocialWorkerProfile;
import com.kgu.life_watch.domain.user.repository.ElderlyProfileRepository;
import com.kgu.life_watch.global.exception.ErrorCode;
import com.kgu.life_watch.global.exception.LifelineException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessageService {

  private final ElderlyProfileRepository elderlyProfileRepository;

  @Transactional(readOnly = true)
  public void sendEmergencyAlert(Long elderlyId, String label, String explanation) {
    ElderlyProfile elderly =
        elderlyProfileRepository
            .findWithSocialWorkerProfileById(elderlyId)
            .orElseThrow(() -> LifelineException.from(ErrorCode.MEMBER_NOT_FOUND));

    SocialWorkerProfile socialWorker = elderly.getSocialWorkerProfile();
    if (socialWorker == null || socialWorker.getUser().getFcmToken() == null) {
      throw LifelineException.from(ErrorCode.FCM_TOKEN_NOT_FOUND);
    }

    Message message =
        Message.builder()
            .putData("title", "응급상황 발생!")
            .putData("body", explanation)
            .setToken(socialWorker.getUser().getFcmToken())
            .build();

    try {
      FirebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException e) {
      throw LifelineException.from(ErrorCode.FCM_SEND_FAILED);
    }
  }

  @Transactional(readOnly = true)
  public void sendMedicineAlarm(MedicineAlarm alarm) {
    String fcmToken = alarm.getUser().getFcmToken();
    if (fcmToken == null) {
      throw LifelineException.from(ErrorCode.FCM_TOKEN_NOT_FOUND);
    }

    // 띄어쓰기로 가독성 향상
    Message message =
        Message.builder()
            .putData("title", "약 복용 알림")
            .putData(
                "body",
                "약 이름: "
                    + alarm.getMedicineName()
                    + "\n복용 시간: "
                    + alarm.getTime().toString()
                    + (alarm.getMedicineNote() != null ? "\n주의사항: " + alarm.getMedicineNote() : ""))
            .setToken(fcmToken)
            .build();

    try {
      FirebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException e) {
      throw LifelineException.from(ErrorCode.FCM_SEND_FAILED);
    }
  }
}
