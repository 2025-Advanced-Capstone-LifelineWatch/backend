package com.kgu.life_watch.domain.user.entity;

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
    private String loginId;  // <- 로그인용 ID

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // 암호화 저장됨 (BCrypt)

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, name = "resident_registration_number")
    private String rrn;

    @Column(nullable = false, name = "disability_registration_number")
    private String drn;

    @Column(nullable = false, name = "social_worker_id")
    private Long socialWorkerId;

    @Column(nullable = false)
    private String birthDate;

    @Column(nullable = false)
    private String gender;

    @Column
    private String protectorContact; // 독거노인 전용

    @Column(name = "assigned_elder_id")
    private Long assignedElderId; // 사회복지사 전용 (optional)

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER,
        ADMIN,
        SOCIAL_WORKER
    }
    /**
     *
     * 필수 :
     * 이름,
     * 아이디,
     * 패스워드,
     * 생년월일(YYYY/MM/DD),
     * 성별,
     * 주소,
     * 주민번호,
     * 담당사회복지사 아이디,
     * 핸드폰 번호
     * 보호자 연락처
     *
     *
     * 필수 :
     * 이름,
     * 아이디,
     * 패스워드,
     * 생년월일(YYYY/MM/DD),
     * 성별,
     * 주소,
     * 주민번호,
     * 핸드폰 번호
     *
     * 선택 :
     * 담당환자 아이디
     */
}