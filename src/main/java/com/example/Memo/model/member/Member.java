package com.example.Memo.model.member;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 사용자 정보,
 */
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate //(update 시 null 인필드 제외)
@Table(indexes = {@Index(name = "member_user_name_index", columnList = "userName", unique = true)})
public class Member {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName; // 계정 아이디, email, phone

    @Column(nullable = false)
    private String name; // 이름

    private String password;

    @Column
    private String image; // 프로필 이미지

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 권한

    private String phone; // 전화번호

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    public Member(Long memberId, String username, Role role) {
        this.id = memberId;
        this.userName = username;
        this.role = role;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }
}
