package com.example.Memo.web.dto;

import com.example.Memo.util.RequestUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseDto {
    private String msg = RequestUtil.REQUEST_SUCCESS_MSG;
    private Integer code = RequestUtil.REQUEST_SUCCESS_CODE;

    @Builder
    public ResponseDto(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public static com.example.Memo.web.dto.ResponseDto makeSuccessResponseStatus() {
        return com.example.Memo.web.dto.ResponseDto.builder()
                .msg(RequestUtil.REQUEST_SUCCESS_MSG)
                .code(RequestUtil.REQUEST_SUCCESS_CODE)
                .build();
    }
}
