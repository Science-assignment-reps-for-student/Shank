package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.BoardContentResponse;
import kr.hs.dsm_scarfs.shank.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ApplicationListResponse boardList(@RequestParam("class_number") @Nullable Integer classNumber,
                                             Pageable page) {
        return boardService.getBoardList(classNumber, page);
    }

    @PostMapping
    public Integer writeBoard(@RequestParam String title,
                           @RequestParam String content,
                           @RequestParam("class_number") String classNumber,
                           MultipartFile[] images) {
        return boardService.writeBoard(title, content, classNumber, images);
    }

    @GetMapping("/{boardId}")
    public BoardContentResponse getBoardContent(@PathVariable Integer boardId) {
        return boardService.getBoardContent(boardId);
    }

    @PutMapping("/{boardId}")
    public Integer changeBoard(@PathVariable Integer boardId,
                            @RequestParam String title,
                            @RequestParam String content,
                            MultipartFile[] images) {
        return boardService.changeBoard(boardId, title, content, images);
    }


    @DeleteMapping("/{boardId}")
    public void deleteBoardContent(@PathVariable Integer boardId) {
        boardService.deleteBoard(boardId);
    }

}
