package com.example.Memo.web.controller;

import com.example.Memo.model.Board;
import com.example.Memo.repository.BoardRepository;
import com.example.Memo.web.dto.PagingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PageController {

  @Autowired
  BoardRepository boardRepository;

  // '/board/page?pageNumber=1&size=10'
  @GetMapping("/board/page")
  public Page<PagingDto> paging(@PageableDefault(size = 10, sort = "createdDate") Pageable pageRequest) {
    Page<Board> boardList = boardRepository.findAll(pageRequest);

    Page<PagingDto> pagingList = boardList.map(
            board -> new PagingDto(
                    board.getId(),
                    board.getTitle(),
                    board.getWriter(),
                    board.getCreatedDate(),
                    board.getModifiedDate()
            ));
    return pagingList;
  }

  // '/board/page/search?title=test&content=test&pageNumber=1&size=10'
  @GetMapping("/board/page/search")
  public Page<PagingDto> searchPaging(@RequestParam(required = false) String title, @RequestParam(required = false) String content,
                                    @PageableDefault(size = 10, sort = "createdDate") Pageable pageRequest) {
    Page<Board> boardList = boardRepository.findAllSearch(title, content, pageRequest);

    Page<PagingDto> pagingList = boardList.map(
            board -> new PagingDto(
                    board.getId(),
                    board.getTitle(),
                    board.getWriter(),
                    board.getCreatedDate(),
                    board.getModifiedDate()
            ));
    return pagingList;
  }
}
