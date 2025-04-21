package com.kgu.life_watch.domain.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.notification.dto.MedicineAlarmRequest;
import com.kgu.life_watch.domain.notification.entity.MedicineAlarm;
import com.kgu.life_watch.domain.notification.service.MedicineAlarmService;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class AlarmController {

  private final MedicineAlarmService medicineAlarmService;

  /** 알람 등록 */
  @PostMapping("/register")
  public ResponseEntity<Void> registerAlarm(@RequestBody MedicineAlarmRequest request) {
    medicineAlarmService.registerAlarm(request);
    return ResponseEntity.ok().build();
  }

  /** 알람 목록 조회 */
  @GetMapping("/list/{userId}")
  public ResponseEntity<List<MedicineAlarm>> getAlarms(@PathVariable Long userId) {
    List<MedicineAlarm> alarms = medicineAlarmService.getAlarms(userId);
    return ResponseEntity.ok(alarms);
  }

  /** 복용 완료 처리 */
  @PatchMapping("/{alarmId}/complete")
  public ResponseEntity<Void> completeAlarm(@PathVariable Long alarmId) {
    medicineAlarmService.markAsCompleted(alarmId);
    return ResponseEntity.ok().build();
  }
}
