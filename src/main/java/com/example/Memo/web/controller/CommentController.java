package com.example.Memo.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.Memo.model.Board;
import com.example.Memo.model.Comment;
import com.example.Memo.repository.BoardRepository;
import com.example.Memo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CommentController {

  BoardRepository boardRepository;
  CommentRepository commentRepository;

  @Autowired
  public CommentController(BoardRepository boardRepository, CommentRepository commentRepository) {
    this.boardRepository = boardRepository;
    this.commentRepository = commentRepository;
  }

  @GetMapping("/board/{id}/comment")
  public List<Comment> getBoardComments(@PathVariable Long id) {
    Board board = boardRepository.findById(id).get();
    return commentRepository.findCommentsByBoard(board);
  }

  @PostMapping("/board/{id}/comment")
  public Comment createComment(@PathVariable Long id, @RequestBody Comment res) {
    Optional<Board> boardItem = boardRepository.findById(id);
    res.setBoard(boardItem.get());
    commentRepository.save(res);
    return res;
  }

  @PutMapping("/board/{id}/comment/{commentId}")
  public Comment updateComment(@PathVariable Long id, @PathVariable Long commentId, @RequestBody Comment res) {
    Optional<Board> boardItem = boardRepository.findById(id);
    res.setBoard(boardItem.get());
    Comment newComment = commentRepository.findById(commentId).get();
    newComment.setTitle(res.getTitle());
    newComment.setContent(res.getContent());
    newComment.setWriter(res.getWriter());
    return newComment;
  }

  @DeleteMapping("/board/{id}/comment/{commentId}")
  public String deleteComment(@PathVariable Long id, @PathVariable Long commentId) {
    commentRepository.deleteById(commentId);
    return "Comment Delete Success!";
  }
}
