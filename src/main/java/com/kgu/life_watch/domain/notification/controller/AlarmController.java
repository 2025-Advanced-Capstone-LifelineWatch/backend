package com.kgu.life_watch.domain.notification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.notification.dto.MedicineAlarmDto;
import com.kgu.life_watch.domain.notification.dto.MedicineAlarmRequest;
import com.kgu.life_watch.domain.notification.service.MedicineAlarmService;
import com.kgu.life_watch.global.domain.SuccessCode;
import com.kgu.life_watch.global.dto.response.ApiResponse;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class AlarmController {

  private final MedicineAlarmService medicineAlarmService;

  /** 알람 등록 */
  @PostMapping("/register")
  public ApiResponse<Void> registerAlarm(@RequestBody MedicineAlarmRequest request) {
    medicineAlarmService.registerAlarm(request);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }

  /** 알람 목록 조회 */
  @GetMapping("/list/{userId}")
  public ApiResponse<MedicineAlarmDto> getAlarms(@PathVariable Long userId) {
    List<MedicineAlarmDto> alarmDtos = medicineAlarmService.getAlarms(userId);
    return new ApiResponse<>(alarmDtos);
  }

  /** 복용 완료 처리 */
  @PatchMapping("/{alarmId}/complete")
  public ApiResponse<Void> completeAlarm(@PathVariable Long alarmId) {
    medicineAlarmService.markAsCompleted(alarmId);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }
}
