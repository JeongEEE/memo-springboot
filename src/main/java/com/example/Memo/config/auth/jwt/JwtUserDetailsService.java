package com.example.Memo.config.auth.jwt;

import com.example.Memo.config.auth.CustomSecurityMember;
import com.example.Memo.model.member.Member;
import com.example.Memo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    public final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserName(username).orElseThrow(
                () -> new UsernameNotFoundException("user not found. username:" + username));
        return new CustomSecurityMember(member);
    }
}