package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.response.BoardListResponse;
import kr.hs.dsm_scarfs.shank.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public BoardListResponse boardList(Pageable page) {
        return boardService.getBoardList(page);
    }

}
