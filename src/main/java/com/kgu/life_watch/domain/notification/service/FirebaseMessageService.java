package com.kgu.life_watch.domain.notification.service;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.kgu.life_watch.domain.notification.entity.MedicineAlarm;
import com.kgu.life_watch.domain.user.entity.ElderlyProfile;
import com.kgu.life_watch.domain.user.entity.SocialWorkerProfile;
import com.kgu.life_watch.domain.user.repository.ElderlyProfileRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessageService {

  private final ElderlyProfileRepository elderlyProfileRepository;

  public void sendEmergencyAlert(Long elderlyId, String label, String explanation) {
    // 노인 조회 (+ 사회복지사도 함께 fetch)
    ElderlyProfile elderly =
        elderlyProfileRepository
            .findWithSocialWorkerProfileById(elderlyId)
            .orElseThrow(() -> new IllegalArgumentException("해당 노인을 찾을 수 없습니다."));

    // 담당 사회복지사 확인
    SocialWorkerProfile socialWorker = elderly.getSocialWorkerProfile();
    if (socialWorker == null || socialWorker.getUser().getFcmToken() == null) {
      log.warn("노인 [{}] 에게 할당된 사회복지사 또는 FCM 토큰이 없습니다.", elderlyId);
      return;
    }

    // 푸시 메시지 생성 및 전송
    Message message =
        Message.builder()
            .putData("title", "응급상황 발생!")
            .putData("body", explanation)
            .setToken(socialWorker.getUser().getFcmToken())
            .build();

    try {
      FirebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException e) {
      log.error("FCM 메시지 전송 실패: {}", e.getMessage(), e);
    }
  }

  public void sendMedicineAlarm(MedicineAlarm alarm) {
    String fcmToken = alarm.getUser().getFcmToken();
    if (fcmToken == null) {
      log.warn("[FCM] 보보자 통신을 위한 FCM Token 없음");
      return;
    }

    Message message =
        Message.builder()
            .putData("title", "약 복용 알림")
            .putData(
                "body",
                alarm.getMedicineName()
                    + " 복용 시간: "
                    + alarm.getTime().toString()
                    + (alarm.getMedicineNote() != null
                        ? " | 주의사항: " + alarm.getMedicineNote()
                        : ""))
            .setToken(fcmToken)
            .build();

    try {
      FirebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException e) {
      log.error("FCM 메시지 전송 실패: {}", e.getMessage(), e);
    }
  }
}
