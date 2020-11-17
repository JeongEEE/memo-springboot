package com.example.Memo.model;

import com.example.Memo.web.dto.BoardDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Board {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "board_id")
  private Long id;

  private String title;
  private String content;

  private String writer;

  @CreationTimestamp
  private LocalDateTime createdDate;

  @UpdateTimestamp
  private LocalDateTime modifiedDate;

  public Board() {

  }

  @Builder
  public Board(String title, String content, String writer) {
    this.title = title;
    this.content = content;
    this.writer = writer;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    Board board = (Board) o;
    return Objects.equals(id, board.id) &&
            Objects.equals(title, board.title) &&
            Objects.equals(content, board.content) &&
            Objects.equals(writer, board.writer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, content, writer);
  }

  public Board update(BoardDto newBoardDto) {
    this.title = newBoardDto.getTitle();
    this.content = newBoardDto.getContent();
    return this;
  }
}
