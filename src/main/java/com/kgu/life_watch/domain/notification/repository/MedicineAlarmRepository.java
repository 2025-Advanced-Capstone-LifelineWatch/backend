package com.kgu.life_watch.domain.notification.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kgu.life_watch.domain.notification.entity.MedicineAlarm;

public interface MedicineAlarmRepository extends JpaRepository<MedicineAlarm, Long> {

  @Query(
      "SELECT a FROM MedicineAlarm a JOIN FETCH a.user WHERE a.time >= :start AND a.time < :end AND a.status = 'SCHEDULED'")
  List<MedicineAlarm> findAlarmsByTimeBetween(
      @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query("SELECT m FROM MedicineAlarm m WHERE m.user.id = :userId")
  List<MedicineAlarm> findAllByUserId(Long userId);

  List<MedicineAlarm> findAllByAlarmGroupId(Long alarmGroupId);
}
