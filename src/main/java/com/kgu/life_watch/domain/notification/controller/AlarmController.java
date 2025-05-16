package com.kgu.life_watch.domain.notification.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.notification.dto.MedicineAlarmDto;
import com.kgu.life_watch.domain.notification.dto.MedicineAlarmRequest;
import com.kgu.life_watch.domain.notification.service.MedicineAlarmService;
import com.kgu.life_watch.global.domain.SuccessCode;
import com.kgu.life_watch.global.dto.response.ApiResponse;
import com.kgu.life_watch.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
@Tag(name = "AlarmController", description = "복용 약 알람 관련 API")
public class AlarmController {

  private final MedicineAlarmService medicineAlarmService;

  /** 알람 등록 */
  @PostMapping("/register")
  @Operation(summary = "복용 약 알람 등록 API", description = "복용하는 약 알람을 등록하는 API입니다.")
  public ApiResponse<Void> registerAlarm(
      @RequestBody MedicineAlarmRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    medicineAlarmService.registerAlarm(request, userDetails.user());
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }

  /** 알람 목록 조회 */
  @GetMapping("/list")
  @Operation(summary = "복용 약 알람 목록 조회 API", description = "복용하는 약 알람 목록을 조회하는 API입니다.")
  public ApiResponse<MedicineAlarmDto> getAlarms(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    List<MedicineAlarmDto> alarmDtos = medicineAlarmService.getAlarms(userDetails.user());
    return new ApiResponse<>(alarmDtos);
  }

  /** 알람 삭제 기능 */
  @DeleteMapping("/{alarmId}")
  @Operation(summary = "복용 약 알람 삭제 ", description = "복용하는 약 알람을 삭제하는 API입니다.")
  public ApiResponse<Void> deleteAlarm(@PathVariable Long alarmId) {
    medicineAlarmService.deleteAlarm(alarmId);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }

  /** 복용 완료 처리 */
  @PatchMapping("/{alarmId}/complete")
  @Operation(summary = "복용 약 완료 처리 API", description = "복용하는 약을 완료 처리하는 API입니다.")
  public ApiResponse<Void> completeAlarm(@PathVariable Long alarmId) {
    medicineAlarmService.markAsCompleted(alarmId);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }
}
