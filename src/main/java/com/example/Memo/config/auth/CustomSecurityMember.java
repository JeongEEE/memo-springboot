package com.example.Memo.config.auth;

import com.example.Memo.model.member.Member;
import com.example.Memo.model.member.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 전통적인 id/pw 이용시 사용하는 user detail 객체
 */
@Getter
@Setter
public class CustomSecurityMember extends User {

    private Member member;

    public CustomSecurityMember(Member member) {
        super(member.getUserName(),
                member.getPassword(),
                makeGrantedeAuth(member.getRole()));

        this.member = member;
    }

    private static boolean isCredentialNonExpired(LocalDateTime passwordModifiedDate) {
        if (passwordModifiedDate != null && passwordModifiedDate.plusMonths(3l).isBefore(LocalDateTime.now())) {
            // 비밀번호 변경한지 3개월이 지났다면 return false;
            return false;
        }
        return true;
    }

    private static List<GrantedAuthority> makeGrantedeAuth(Role role) {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(role.getKey()));
        return list;
    }
}
