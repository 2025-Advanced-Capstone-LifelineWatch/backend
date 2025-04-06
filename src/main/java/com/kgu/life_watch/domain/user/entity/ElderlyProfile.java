package com.kgu.life_watch.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ElderlyProfile {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, name = "disability_registration_number")
    private String drn; // 장애등록번호 필수

    @Column(nullable = false)
    private Long socialWorkerId; // 담당 사회복지사 ID 필수

    @Column(nullable = false)
    private String protectorContact; // 보호자 연락처 필수

    public void setUser(User user) {
        this.user = user;
    }
}
