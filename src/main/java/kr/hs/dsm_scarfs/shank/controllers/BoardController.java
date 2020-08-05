package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.BoardRequest;
import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.BoardContentResponse;
import kr.hs.dsm_scarfs.shank.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ApplicationListResponse boardList(Pageable page) {
        return boardService.getBoardList(page);
    }

    @GetMapping("/{boardId}")
    public BoardContentResponse getBoardContent(@PathVariable Integer boardId) {
        return boardService.getBoardContent(boardId);
    }

    @PostMapping("/write")
    public void writeBoard(@RequestParam BoardRequest boardWrite, MultipartFile[] files) {
        boardService.writeBoard(boardWrite, files);
    }

    @PutMapping("/{boardId}")
    public void changeBoard(@PathVariable Integer boardId, @RequestParam BoardRequest boardRequest, MultipartFile[] files) {
        boardService.changeBoard(boardId, boardRequest, files);
    }


    @DeleteMapping("/{boardId}")
    public void deleteBoardContent(@PathVariable Integer boardId) {
        boardService.deleteBoard(boardId);
    }

}
