package com.kgu.life_watch.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.notification.dto.MedicineAlarmRequest;
import com.kgu.life_watch.domain.notification.entity.MedicineAlarm;
import com.kgu.life_watch.domain.notification.entity.MedicineAlarm.AlarmStatus;
import com.kgu.life_watch.domain.notification.repository.MedicineAlarmRepository;
import com.kgu.life_watch.domain.user.entity.User;
import com.kgu.life_watch.domain.user.repository.UserRepository;
import com.kgu.life_watch.global.exception.ErrorCode;
import com.kgu.life_watch.global.exception.LifelineException;

@Service
@RequiredArgsConstructor
public class MedicineAlarmService {
  private final MedicineAlarmRepository medicineAlarmRepository;
  private final UserRepository userRepository;

  // 약 알람 등록
  @Transactional
  public void registerAlarm(MedicineAlarmRequest request) {
    User user =
        userRepository
            .findById(request.userId())
            .orElseThrow(() -> LifelineException.from(ErrorCode.MEMBER_NOT_FOUND));

    MedicineAlarm alarm =
        MedicineAlarm.builder()
            .user(user)
            .medicineName(request.medicineName())
            .time(request.time())
            .medicineNote(request.medicineNote())
            .status(AlarmStatus.SCHEDULED)
            .build();

    medicineAlarmRepository.save(alarm);
  }

  // 약 알람 조회
  @Transactional(readOnly = true)
  public List<MedicineAlarm> getAlarms(Long userId) {
    return medicineAlarmRepository.findAllByUserId(userId);
  }

  // 약 알람 복용 완료 처리
  @Transactional
  public void markAsCompleted(Long alarmId) {
    MedicineAlarm alarm =
        medicineAlarmRepository
            .findById(alarmId)
            .orElseThrow(() -> LifelineException.from(ErrorCode.ALARM_NOT_FOUND));
    alarm.updateStatus(AlarmStatus.COMPLETE);
  }
}
