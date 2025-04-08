package com.kgu.life_watch.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SocialWorkerProfile {

  @Id private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "assigned_elder_id")
  private Long assignedElderId; // 담당 독거노인 ID 선택적 필드

  public void setUser(User user) {
    this.user = user;
  }
}
