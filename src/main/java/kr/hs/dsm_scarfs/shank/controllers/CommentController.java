package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.CommentRequest;
import kr.hs.dsm_scarfs.shank.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public void postComment(@PathVariable Integer boardId, @RequestBody CommentRequest commentRequest) {
        commentService.postComment(boardId, commentRequest);
    }

    @PutMapping("/{commentId}")
    public void chageComment(@PathVariable Integer commentId, @RequestBody CommentRequest commentRequest) {
        commentService.changeComment(commentId, commentRequest);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
    }

    @PostMapping("/sub/{commentId}")
    public void postCocomment(@PathVariable Integer commentId, @RequestBody CommentRequest commentRequest) {
        commentService.postCocomment(commentId, commentRequest);
    }

    @PutMapping("/sub/{cocommentId}")
    public void chageCocomment(@PathVariable Integer cocommentId, @RequestBody CommentRequest commentRequest) {
        commentService.changeCocomment(cocommentId, commentRequest);
    }

    @DeleteMapping("/sub/{cocommentId}")
    public void deleteCocomment(@PathVariable Integer cocommentId) {
        commentService.deleteCocomment(cocommentId);
    }

}
