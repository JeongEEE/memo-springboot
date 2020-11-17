package com.example.Memo.web.controller;

import com.example.Memo.model.Board;
import com.example.Memo.service.BoardService;
import com.example.Memo.web.dto.BoardDto;
import com.example.Memo.web.dto.ResponseDto;
import com.example.Memo.web.dto.ResponseWithObjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardController {

  private BoardService boardService;

  @Autowired
  public BoardController(BoardService boardService) {
    this.boardService = boardService;
  }

  @GetMapping("/board")
  public ResponseDto list() {
    List<BoardDto> boardDtoList = boardService.getBoardList();
    ResponseWithObjectDto responseWithObjectDto = new ResponseWithObjectDto(boardDtoList);
    return responseWithObjectDto;
  }

  @PostMapping("/board")
  public ResponseDto add(@RequestBody BoardDto boardDto) {
    boardService.saveBoard(boardDto);
    return ResponseDto.makeSuccessResponseStatus();
  }

  @GetMapping("/board/{id}")
  public ResponseDto findOne(@PathVariable Long id) {
    BoardDto boardDto = boardService.getBoardDto(id);
    ResponseWithObjectDto responseWithObjectDto = new ResponseWithObjectDto(boardDto);
    return responseWithObjectDto;
  }

  @PutMapping("/board/{id}")
  public ResponseDto update(@PathVariable Long id, @RequestBody BoardDto newBoardDto) {
//    String title = res.get("title").toString();
//    String content = res.get("content").toString();
//    Optional<Board> board = boardRepository.findById(id);
//    board.get().setTitle(title);
//    board.get().setContent(content);
//    return boardRepository.save(board.get());
    Board board = boardService.getBoard(id);
    boardService.updateBoard(board, newBoardDto);
    return ResponseDto.makeSuccessResponseStatus();
  }

  @DeleteMapping("/board/{id}")
  public ResponseDto delete(@PathVariable Long id) {
    boardService.deleteBoard(id);
    return ResponseDto.makeSuccessResponseStatus();
  }
}
