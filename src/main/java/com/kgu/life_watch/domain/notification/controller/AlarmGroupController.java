package com.kgu.life_watch.domain.notification.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.notification.dto.AlarmGroupDto;
import com.kgu.life_watch.domain.notification.service.AlarmGroupService;
import com.kgu.life_watch.global.domain.SuccessCode;
import com.kgu.life_watch.global.dto.response.ApiResponse;
import com.kgu.life_watch.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/alarm/group")
@RequiredArgsConstructor
public class AlarmGroupController {
  private final AlarmGroupService alarmGroupService;

  @GetMapping
  @Operation(summary = "그룹 조회 ", description = "복용하는 약 알람을 그룹 조회입니다.")
  public ApiResponse<AlarmGroupDto> getGroups(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return new ApiResponse<>(alarmGroupService.getAlarmGroups(userDetails.user()));
  }

  @DeleteMapping("/{groupId}")
  @Operation(summary = "복용 약 그룹 삭제 ", description = "복용하는 약 그룹을 삭제하는 API입니다.")
  public ApiResponse<Void> deleteGroup(@PathVariable Long groupId) {
    alarmGroupService.deleteGroup(groupId);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }

  @PatchMapping("/{groupId}")
  @Operation(summary = "복용 약 그룹 갱신 ", description = "복용하는 약 그룹을 갱신하는 API입니다.")
  public ApiResponse<Void> updateGroup(
      @PathVariable Long groupId,
      @RequestParam @NotBlank String newName,
      @RequestParam String newNote) {
    alarmGroupService.updateGroup(groupId, newName, newNote);
    return new ApiResponse<>(SuccessCode.REQUEST_OK);
  }
}
