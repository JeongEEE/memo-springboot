package com.example.Memo.repository;

import com.example.Memo.model.Board;
import com.example.Memo.model.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
  List<Comment> findCommentsByBoard(Board board);
}
