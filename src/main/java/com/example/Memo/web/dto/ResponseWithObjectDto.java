package com.example.Memo.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor //json 생성을 위한 모든 args 생성자
public class ResponseWithObjectDto extends ResponseDto {
    Object data;

    public ResponseWithObjectDto(Object data) {
        this.data = data;
    }
}
