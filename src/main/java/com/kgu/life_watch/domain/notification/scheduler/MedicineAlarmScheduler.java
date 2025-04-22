package com.kgu.life_watch.domain.notification.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.kgu.life_watch.domain.notification.entity.MedicineAlarm;
import com.kgu.life_watch.domain.notification.repository.MedicineAlarmRepository;
import com.kgu.life_watch.domain.notification.service.FirebaseMessageService;

@Component
@RequiredArgsConstructor
@Slf4j
public class MedicineAlarmScheduler {

  private final MedicineAlarmRepository medicineAlarmRepository;
  private final FirebaseMessageService firebaseMessageService;

  @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
  // 실행될 때마다 현재 시각 (LocalTime.now()) 과 같은 시간에 설정된
  // 복용 알람 데이터(MedicineAlarm 엔티티) 를 MedicineAlarmRepository에서 조회함.
  public void sendMedicineAlarms() {
    LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
    List<MedicineAlarm> alarms = medicineAlarmRepository.findAlarmsByTime(now);
    for (MedicineAlarm alarm : alarms) {
      firebaseMessageService.sendMedicineAlarm(alarm);
    }
  }
}
