package com.example.Memo.model.member;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * oauth2 refresh token 유지테이블
 * 굳이 유지할 필요는 없지만 향후 구글, 네이버 api 를 쓸수도 있음으로 유지한다.
 */
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MemberRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", unique = true)
    private Member member;

    @Column(nullable = false)
    private String registrationId;

    @Column(nullable = false)
    private String refreshToken;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
