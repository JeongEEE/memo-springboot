package com.example.Memo.web.dto.member;

import com.example.Memo.model.member.Member;
import com.example.Memo.model.member.Role;
import com.example.Memo.util.RegexUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberJoinRequestDto {
    @NotNull(message = "이름 입력 필수")
    private String name;

    @NotNull(message = "이메일 입력 필수")
    @Pattern(regexp = RegexUtil.EMAIL_REGEXP, message = "유효하지 않은 이메일입니다")
    private String email;

    @NotNull(message = "전화번호 입력 필수")
    @Pattern(regexp = RegexUtil.PHONE_REGEXP, message = "유효하지 않은 휴대전화번호입니다")
    private String phone;

    @NotNull(message = "비밀번호 입력 필수")
    @Pattern(regexp = RegexUtil.PASSWORD_REGEXP, message = "유효하지 않은 비밀번호입니다")
    private String password;

    @NotNull(message = "권한 입력 필수")
    private String role; //ADMIN, USER, GUEST

    public Member toEntity(String encryptPassword) {
        return Member.builder()
                .name(this.name)
                .userName(this.email)
                .role(Role.valueOf(role))
                .phone(phone)
                .password(encryptPassword)
                .build();
    }

    public void checkJoinUser(String secret) {
        if (role.equals(Role.ADMIN.name())) {
            return;
        }
    }
}