package com.example.Memo.repository;

import com.example.Memo.model.member.Member;
import com.example.Memo.model.member.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserName(String userName);

    List<Member> findAllByRole(Role role);
}
