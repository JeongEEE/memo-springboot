package com.example.Memo.service;

import com.example.Memo.exception.ObjectNonexistentException;
import com.example.Memo.model.member.Member;
import com.example.Memo.model.member.MemberRefreshToken;
import com.example.Memo.repository.MemberRefreshTokenRepository;
import com.example.Memo.repository.MemberRepository;
import com.example.Memo.web.dto.member.MemberJoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(MemberJoinRequestDto requestDto) {
        String encryptPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = requestDto.toEntity(encryptPassword);
        member = memberRepository.save(member);
        return member;
    }

    @Transactional
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new ObjectNonexistentException("해당 member 가 존재하지 않습니다. id:" + id));
        return member;
    }

    @Transactional(readOnly = true)
    public Member findMemberByUserName(String userName) {
        Member member = memberRepository.findByUserName(userName).orElseThrow(
                () -> new ObjectNonexistentException("해당 member 가 존재하지 않습니다. username:" + userName));
        return member;
    }

    @Transactional
    public MemberRefreshToken upsertRefreshToken(Member member, String refreshToken, String registrationId) {
        MemberRefreshToken memberRefreshToken = memberRefreshTokenRepository
                .findByMemberId(member.getId())
                .orElse(MemberRefreshToken.builder()
                        .member(member)
                        .refreshToken(refreshToken)
                        .registrationId(registrationId)
                        .build());
        memberRefreshToken.updateRefreshToken(refreshToken);
        return memberRefreshTokenRepository.save(memberRefreshToken);
    }
}
