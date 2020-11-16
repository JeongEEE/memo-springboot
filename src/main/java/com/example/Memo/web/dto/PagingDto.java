package com.example.Memo.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PagingDto {
  private Long id;
  private String title;
  private String writer;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;

  public PagingDto(Long id, String title, String writer, LocalDateTime createdDate, LocalDateTime modifiedDate) {
    this.id = id;
    this.title = title;
    this.writer = writer;
    this.createdDate = createdDate;
    this.modifiedDate = modifiedDate;
  }
}
