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

    @PutMapping("/{commentId}")
    public void chageComment(@PathVariable Integer commentId, @RequestBody String content) {
        commentService.changeComment(commentId, content);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
    }

    @PostMapping("/sub/{commentId}")
    public void postCocomment(@PathVariable Integer commentId, @RequestBody String content) {
        commentService.postCocomment(commentId, content);
    }

    @PutMapping("/sub/{cocommentId}")
    public void chageCocomment(@PathVariable Integer cocommentId, @RequestBody String content) {
        commentService.changeCocomment(cocommentId, content);
    }

    @DeleteMapping("/sub/{cocommentId}")
    public void deleteCocomment(@PathVariable Integer cocommentId) {
        commentService.deleteCocomment(cocommentId);
    }

}
