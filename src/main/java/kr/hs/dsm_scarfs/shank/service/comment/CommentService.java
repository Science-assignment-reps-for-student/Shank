package kr.hs.dsm_scarfs.shank.service.comment;

public interface CommentService {
    void postComment(Integer boardId, String content);
    void postCocomment(Integer commentId, String content);
    void changeComment(Integer commentId, String content);
    void changeCocomment(Integer cocommentId, String content);
    void deleteComment(Integer commentId);
    void deleteCocomment(Integer cocommentId);
}
