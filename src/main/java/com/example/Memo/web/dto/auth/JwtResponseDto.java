package com.example.Memo.web.dto.auth;

import com.example.Memo.model.member.Role;
import com.example.Memo.web.dto.ResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class JwtResponseDto extends ResponseDto implements Serializable {
    private String token;
    private Role role;
    private LocalDateTime loginTime;

    public JwtResponseDto(String token, Role role, LocalDateTime loginTime) {
        this.token = token;
        this.role = role;
        this.loginTime = loginTime;
    }

    public JwtResponseDto(String msg, Integer code, String token, Role role, LocalDateTime loginTime) {
        super(msg, code);
        this.token = token;
        this.role = role;
        this.loginTime = loginTime;
    }

}