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

  @Scheduled(cron = "0 * * * * *") // 매 분 실행
  public void sendAndScheduleMedicineAlarms() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime nextMinute = now.plusMinutes(1);

    LocalDateTime from = now.withSecond(0).withNano(0);
    LocalDateTime to = nextMinute.withSecond(0).withNano(0);

    List<MedicineAlarm> alarms = medicineAlarmRepository.findAlarmsByTimeBetween(from, to);

    for (MedicineAlarm alarm : alarms) {
      firebaseMessageService.sendMedicineAlarm(alarm);

      switch (alarm.getRepeatCycle()) {
        case DAILY -> createNextAlarm(alarm, alarm.getTime().plusDays(1));
        case EVERY_OTHER_DAY -> createNextAlarm(alarm, alarm.getTime().plusDays(2));
        case WEEKLY -> createNextAlarm(alarm, alarm.getTime().plusWeeks(1));
        default -> {} // ONCE는 반복 없음
      }
    }
  }

  private void createNextAlarm(MedicineAlarm current, LocalDateTime nextTime) {
    MedicineAlarm nextAlarm =
        MedicineAlarm.builder()
            .user(current.getUser())
            .medicineName(current.getMedicineName())
            .medicineNote(current.getMedicineNote())
            .time(nextTime)
            .status(MedicineAlarm.AlarmStatus.SCHEDULED)
            .repeatCycle(current.getRepeatCycle())
            .build();
    medicineAlarmRepository.save(nextAlarm);
  }
}
