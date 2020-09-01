package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.CommentRequest;
import kr.hs.dsm_scarfs.shank.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public void postComment(@PathVariable Integer boardId,
                            @RequestBody @Valid CommentRequest commentRequest) {
        commentService.postComment(boardId, commentRequest);
    }

    @PutMapping("/{commentId}")
    public Integer changeComment(@PathVariable Integer commentId,
                                 @RequestBody @Valid CommentRequest commentRequest) {
        return commentService.changeComment(commentId, commentRequest);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
    }

    @PostMapping("/sub/{commentId}")
    public void postSubComment(@PathVariable Integer commentId,
                               @RequestBody @Valid CommentRequest commentRequest) {
        commentService.postSubComment(commentId, commentRequest);
    }

    @PutMapping("/sub/{subCommentId}")
    public Integer changeSubComment(@PathVariable Integer subCommentId,
                                    @RequestBody @Valid CommentRequest commentRequest) {
        return commentService.changeSubComment(subCommentId, commentRequest);
    }

    @DeleteMapping("/sub/{subCommentId}")
    public void deleteSubComment(@PathVariable Integer subCommentId) {
        commentService.deleteSubComment(subCommentId);
    }

}
