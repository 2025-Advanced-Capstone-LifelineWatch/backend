package com.kgu.life_watch.domain.notification.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kgu.life_watch.domain.notification.entity.MedicineAlarm;

public interface MedicineAlarmRepository extends JpaRepository<MedicineAlarm, Long> {

  @Query("SELECT m FROM MedicineAlarm m WHERE m.time = :now")
  List<MedicineAlarm> findAlarmsByTime(LocalDateTime now);

  @Query("SELECT m FROM MedicineAlarm m WHERE m.user.id = :userId")
  List<MedicineAlarm> findAllByUserId(Long userId);
}
