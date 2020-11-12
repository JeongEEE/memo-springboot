package com.example.Memo.config.auth.jwt;

import com.example.Memo.model.member.Member;
import com.example.Memo.model.member.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 만료기한 상관 없이 userdetail 객체를 생성
 * oauth, jwtFilter, passwordExpired 시에 사용
 */
@Getter
public class JwtUserDetails extends User {

    private Member member;

    public JwtUserDetails(Long memberId, String username, String password, Role role, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.member = new Member(memberId, username, role);
    }
}
