package com.kgu.life_watch.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.kgu.life_watch.domain.notification.dto.AlarmGroupDto;
import com.kgu.life_watch.domain.notification.entity.AlarmGroup;
import com.kgu.life_watch.domain.notification.repository.AlarmGroupRepository;
import com.kgu.life_watch.domain.notification.repository.MedicineAlarmRepository;
import com.kgu.life_watch.domain.user.entity.User;
import com.kgu.life_watch.global.exception.ErrorCode;
import com.kgu.life_watch.global.exception.LifelineException;

@Service
@RequiredArgsConstructor
public class AlarmGroupService {
  private final AlarmGroupRepository alarmGroupRepository;
  private final MedicineAlarmRepository medicineAlarmRepository;

  @Transactional(readOnly = true)
  public List<AlarmGroupDto> getAlarmGroups(User user) {
    List<AlarmGroup> groups = alarmGroupRepository.findAllByUser(user);
    return groups.stream()
        .map(
            group ->
                AlarmGroupDto.fromEntity(
                    group, medicineAlarmRepository.findAllByAlarmGroupId(group.getId())))
        .toList();
  }

  @Transactional
  public void deleteGroup(Long groupId) {
    AlarmGroup group =
        alarmGroupRepository
            .findById(groupId)
            .orElseThrow(() -> LifelineException.from(ErrorCode.ALARM_NOT_FOUND));
    alarmGroupRepository.delete(group); // cascade = ALL로 알람도 같이 삭제됨
  }

  @Transactional
  public void updateGroup(Long groupId, String newName, String newNote) {
    AlarmGroup group =
        alarmGroupRepository
            .findById(groupId)
            .orElseThrow(() -> LifelineException.from(ErrorCode.ALARM_NOT_FOUND));
    group.updateInfo(newName, newNote);
  }
}
