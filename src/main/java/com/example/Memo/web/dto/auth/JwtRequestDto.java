package com.example.Memo.web.dto.auth;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * login 시 사용하는 request filed 객체
 */
@Getter
@NoArgsConstructor
public class JwtRequestDto implements Serializable {
    @NotBlank(message = "아이디를 입력해주세요")
    private String username;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    public JwtRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}