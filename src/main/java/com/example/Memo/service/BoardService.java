package com.example.Memo.service;

import com.example.Memo.model.Board;
import com.example.Memo.repository.BoardRepository;
import com.example.Memo.web.dto.BoardDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
  private BoardRepository boardRepository;

  public BoardService(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  @Transactional
  public Long saveBoard(BoardDto boardDto) {
    return boardRepository.save(boardDto.toEntity()).getId();
  }

  @Transactional
  public List<BoardDto> getBoardList() {
    Iterable<Board> boards = boardRepository.findAll();
    List<BoardDto> boardDtoList = new ArrayList<>();

    for(Board board: boards) {
      BoardDto boardDto = BoardDto.builder()
              .id(board.getId())
              .title(board.getTitle())
              .content(board.getContent())
              .writer(board.getWriter())
              .createdDate(board.getCreatedDate())
              .modifiedDate(board.getModifiedDate())
              .build();
      boardDtoList.add(boardDto);
    }

    return boardDtoList;
  }

  @Transactional
  public BoardDto getBoardDto(Long id) {
    Optional<Board> boardWrapper = boardRepository.findById(id);
    Board board = boardWrapper.get();

    BoardDto boardDto = BoardDto.builder()
            .id(board.getId())
            .title(board.getTitle())
            .content(board.getContent())
            .writer(board.getWriter())
            .createdDate(board.getCreatedDate())
            .modifiedDate(board.getModifiedDate())
            .build();

    return boardDto;
  }

  @Transactional
  public Board getBoard(Long id) {
    Optional<Board> board = boardRepository.findById(id);
    return board.get();
  }

  @Transactional
  public void updateBoard(Board board, BoardDto newBoardDto) {
    board.update(newBoardDto);
    boardRepository.save(board);
  }

  @Transactional
  public void deleteBoard(Long id) {
    boardRepository.deleteById(id);
  }
}
