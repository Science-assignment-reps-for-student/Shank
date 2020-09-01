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
    public Integer changeComment(@PathVariable Integer commentId, @RequestBody CommentRequest commentRequest) {
        return commentService.changeComment(commentId, commentRequest);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
    }

    @PostMapping("/sub/{commentId}")
    public void postSubComment(@PathVariable Integer commentId, @RequestBody CommentRequest commentRequest) {
        commentService.postSubComment(commentId, commentRequest);
    }

    @PutMapping("/sub/{cocommentId}")
    public Integer changeSubComment(@PathVariable Integer cocommentId, @RequestBody CommentRequest commentRequest) {
        return commentService.changeSubComment(cocommentId, commentRequest);
    }

    @DeleteMapping("/sub/{cocommentId}")
    public void deleteSubComment(@PathVariable Integer cocommentId) {
        commentService.deleteSubComment(cocommentId);
    }

}
