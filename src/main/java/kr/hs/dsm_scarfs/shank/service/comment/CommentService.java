package kr.hs.dsm_scarfs.shank.service.comment;

import kr.hs.dsm_scarfs.shank.payload.request.CommentRequest;

public interface CommentService {
    void postComment(Integer boardId, CommentRequest commentRequest);
    void postSubComment(Integer commentId, CommentRequest commentRequest);
    void changeComment(Integer commentId, CommentRequest commentRequest);
    void changeSubComment(Integer subCommentId, CommentRequest commentRequest);
    void deleteComment(Integer commentId);
    void deleteSubComment(Integer subCommentId);
}
