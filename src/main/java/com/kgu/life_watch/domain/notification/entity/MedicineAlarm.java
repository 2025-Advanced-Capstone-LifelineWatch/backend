package com.kgu.life_watch.domain.notification.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

import com.kgu.life_watch.domain.user.entity.User;
import com.kgu.life_watch.global.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MedicineAlarm extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "medicine_name", nullable = false)
  private String medicineName;

  @Column(name = "time")
  private LocalDateTime time;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private AlarmStatus status;

  public void updateStatus(AlarmStatus newStatus) {
    this.status = newStatus;
  }

  @Column(name = "medicine_note")
  private String medicineNote;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public enum AlarmStatus {
    SCHEDULED,
    COMPLETE,
    MISSED
  }
}
