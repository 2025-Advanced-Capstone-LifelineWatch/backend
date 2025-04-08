package com.kgu.life_watch.domain.user.entity;

import java.time.LocalDate;

import com.kgu.life_watch.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "`user`")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String loginId;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false, name = "resident_registration_number")
  private String rrn;

  @Column(nullable = false)
  private LocalDate birthDate;

  @Column(nullable = false)
  private String gender;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private ElderlyProfile elderlyProfile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private SocialWorkerProfile socialWorkerProfile;

  public enum Role {
    USER,
    ADMIN,
    SOCIAL_WORKER
  }

  public void setElderlyProfile(ElderlyProfile elderlyProfile) {
    this.elderlyProfile = elderlyProfile;
    elderlyProfile.setUser(this);
  }

  public void setSocialWorkerProfile(SocialWorkerProfile socialWorkerProfile) {
    this.socialWorkerProfile = socialWorkerProfile;
    socialWorkerProfile.setUser(this);
  }
}
