package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public void postComment(@PathVariable Integer boardId, @RequestBody String content) {
        commentService.postComment(boardId, content);
    }

}
