package com.example.Memo.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.Memo.model.Board;
import com.example.Memo.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class BoardController {

  private BoardRepository boardRepository;

  @Autowired
  public BoardController(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  @GetMapping("/board")
  public Iterable<Board> list() {
    return boardRepository.findAll();
  }

  @PostMapping("/board")
  public Board add(@RequestBody JSONObject res) {
    String title = res.get("title").toString();
    String content = res.get("content").toString();
    String writer = res.get("writer").toString();
    return boardRepository.save(new Board(title, content, writer));
  }

  @GetMapping("/board/{id}")
  public Optional<Board> findOne(@PathVariable Long id) {
    return boardRepository.findById(id);
  }

  @PutMapping("/board/{id}")
  public Board update(@PathVariable Long id, @RequestBody JSONObject res) {
    String title = res.get("title").toString();
    String content = res.get("content").toString();
    Optional<Board> board = boardRepository.findById(id);
    board.get().setTitle(title);
    board.get().setContent(content);
    return boardRepository.save(board.get());
  }

  @DeleteMapping("/board/{id}")
  public void delete(@PathVariable Long id) {
    boardRepository.deleteById(id);
  }
}
