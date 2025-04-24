package com.kgu.life_watch.domain.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.notification.dto.EmergencyAlertRequest;
import com.kgu.life_watch.domain.notification.service.FirebaseMessageService;

@RestController
@RequestMapping("/api/alert")
@RequiredArgsConstructor
@Tag(name = "AlertController", description = "건강 관련 경고 알람 관련 API")
public class AlertController {

  private final FirebaseMessageService firebaseMessageService;

  @PostMapping("/emergency")
  @Operation(
      summary = "노인의 이상건강에 관한 경고 알람 API",
      description = "노인의 이상건강에 관한 경고 알람을 담당 사회복지사에게 전달하는 API입니다.")
  public ResponseEntity<Void> handleEmergency(@RequestBody EmergencyAlertRequest request) {
    firebaseMessageService.sendEmergencyAlert(
        request.elderlyId(), request.predictionLabel(), request.explanation());
    return ResponseEntity.ok().build();
  }
}
