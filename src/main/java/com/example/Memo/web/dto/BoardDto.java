package com.example.Memo.web.dto;

import com.example.Memo.model.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
  private Long id;
  private String title;
  private String content;
  private String writer;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;

  public Board toEntity() {
    Board build = Board.builder()
            .title(title)
            .content(content)
            .writer(writer)
            .build();
    return build;
  }

  @Builder
  public BoardDto(Long id, String title, String content, String writer, LocalDateTime createdDate, LocalDateTime modifiedDate) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.writer = writer;
    this.createdDate = createdDate;
    this.modifiedDate = modifiedDate;
  }
}
