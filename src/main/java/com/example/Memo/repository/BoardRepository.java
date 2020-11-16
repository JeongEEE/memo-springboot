package com.example.Memo.repository;

import com.example.Memo.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long> {
  List<Board> findByTitleContaining(String keyword);

  Page<Board> findAll(Pageable pageable);

  @Query(
          value = "SELECT b FROM Board b WHERE b.title LIKE %:title% OR b.content LIKE %:content%",
          countQuery = "SELECT COUNT(b.id) FROM Board b WHERE b.title LIKE %:title% OR b.content LIKE %:content%"
  )
  Page<Board> findAllSearch(String title, String content, Pageable pageable);
}
