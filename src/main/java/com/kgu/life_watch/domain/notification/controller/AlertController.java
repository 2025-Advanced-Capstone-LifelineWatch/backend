package com.kgu.life_watch.domain.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.notification.dto.EmergencyAlertRequest;
import com.kgu.life_watch.domain.notification.service.FirebaseMessageService;

@RestController
@RequestMapping("/api/alert")
@RequiredArgsConstructor
public class AlertController {

  private final FirebaseMessageService firebaseMessageService;

  @PostMapping("/emergency")
  public ResponseEntity<Void> handleEmergency(@RequestBody EmergencyAlertRequest request) {
    firebaseMessageService.sendEmergencyAlert(
        request.elderlyId(), request.predictionLabel(), request.explanation());
    return ResponseEntity.ok().build();
  }
}
