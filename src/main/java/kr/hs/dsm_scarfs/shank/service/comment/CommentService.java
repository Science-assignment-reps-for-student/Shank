package kr.hs.dsm_scarfs.shank.service.comment;

import kr.hs.dsm_scarfs.shank.payload.request.CommentRequest;

public interface CommentService {
    void postComment(Integer boardId, CommentRequest commentRequest);
    void postCocomment(Integer commentId, CommentRequest commentRequest);
    void changeComment(Integer commentId, CommentRequest commentRequest);
    void changeCocomment(Integer cocommentId, CommentRequest commentRequest);
    void deleteComment(Integer commentId);
    void deleteCocomment(Integer cocommentId);
}
