package kr.hs.dsm_scarfs.shank.service.comment;

import kr.hs.dsm_scarfs.shank.payload.request.CommentRequest;

public interface CommentService {
    void postComment(Integer boardId, CommentRequest commentRequest);
    Integer postSubComment(Integer commentId, CommentRequest commentRequest);
    Integer changeComment(Integer commentId, CommentRequest commentRequest);
    Integer changeSubComment(Integer subCommentId, CommentRequest commentRequest);
    void deleteComment(Integer commentId);
    void deleteSubComment(Integer subCommentId);
}
