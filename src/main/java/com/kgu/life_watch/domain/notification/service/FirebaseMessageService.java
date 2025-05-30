package com.kgu.life_watch.domain.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.kgu.life_watch.domain.notification.entity.MedicineAlarm;
import com.kgu.life_watch.domain.notification.entity.NotificationLog;
import com.kgu.life_watch.domain.notification.repository.NotificationLogRepository;
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
  private final NotificationLogRepository notificationLogRepository;

  @Transactional
  public void sendEmergencyAlert(Long elderlyId, String label, String explanation) {
    ElderlyProfile elderly =
        elderlyProfileRepository
            .findByUserId(elderlyId)
            .orElseThrow(() -> LifelineException.from(ErrorCode.MEMBER_NOT_FOUND));

    SocialWorkerProfile socialWorker = elderly.getSocialWorkerProfile();
    if (socialWorker == null || socialWorker.getUser().getFcmToken() == null) {
      throw LifelineException.from(ErrorCode.FCM_TOKEN_NOT_FOUND);
    }

    Message message =
        Message.builder()
            .putData("title", "응급상황 발생!")
            .putData("body", explanation)
            .putData("elderlyId", String.valueOf(elderly.getUser().getId()))
            .putData("elderlyName", elderly.getUser().getName())
            .setToken(socialWorker.getUser().getFcmToken())
            .build();

    try {
      FirebaseMessaging.getInstance().send(message);

      if (isAbnormal(label)) {
        notificationLogRepository.save(NotificationLog.of(elderlyId, label, explanation));
      }
    } catch (FirebaseMessagingException e) {
      throw LifelineException.from(ErrorCode.FCM_SEND_FAILED);
    }
  }

  private boolean isAbnormal(String label) {
    return label != null && (label.contains("Emergency (비정상)"));
  }

  @Transactional
  public void sendMedicineAlarm(MedicineAlarm alarm) {
    String elderlyFcm = alarm.getUser().getFcmToken();
    String body =
        "약 이름: "
            + alarm.getMedicineName()
            + "\n복용 시간: "
            + alarm.getTime().toString()
            + "\n복용량: "
            + alarm.getDosage()
            + "알"
            + (alarm.getMedicineNote() != null ? "\n주의사항: " + alarm.getMedicineNote() : "");

    // 노인에게 알림
    if (elderlyFcm != null) {
      Message elderlyMessage =
          Message.builder()
              .putData("title", "약 복용 알림")
              .putData("body", body)
              .setToken(elderlyFcm)
              .build();
      try {
        FirebaseMessaging.getInstance().send(elderlyMessage);
      } catch (FirebaseMessagingException e) {
        throw LifelineException.from(ErrorCode.FCM_SEND_FAILED);
      }
    }

    // 복지사에게도 알림
    ElderlyProfile elderly =
        elderlyProfileRepository
            .findByUserId(alarm.getUser().getId())
            .orElseThrow(() -> LifelineException.from(ErrorCode.MEMBER_NOT_FOUND));

    SocialWorkerProfile worker = elderly.getSocialWorkerProfile();
    if (worker == null || worker.getUser() == null || worker.getUser().getFcmToken() == null) {
      throw LifelineException.from(ErrorCode.FCM_TOKEN_NOT_FOUND);
    }

    String workerFcm = worker.getUser().getFcmToken();
    Message workerMessage =
        Message.builder()
            .putData("title", "복지 대상자 약 복용 알림")
            .putData("body", body)
            .putData("elderlyId", String.valueOf(elderly.getUser().getId()))
            .putData("elderlyName", elderly.getUser().getName())
            .setToken(workerFcm)
            .build();
    try {
      FirebaseMessaging.getInstance().send(workerMessage);
    } catch (FirebaseMessagingException e) {
      throw LifelineException.from(ErrorCode.FCM_SEND_FAILED);
    }
  }
}
