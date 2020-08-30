package kr.hs.dsm_scarfs.shank.service.comment;

import kr.hs.dsm_scarfs.shank.entites.board.repository.BoardRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.SubComment;
import kr.hs.dsm_scarfs.shank.entites.comment.Comment;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.SubCommentRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CommentRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.exceptions.ApplicationNotFoundException;
import kr.hs.dsm_scarfs.shank.exceptions.CommentNotFoundException;
import kr.hs.dsm_scarfs.shank.exceptions.PermissionDeniedException;
import kr.hs.dsm_scarfs.shank.payload.request.CommentRequest;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final AuthenticationFacade authenticationFacade;
    private final UserFactory userFactory;

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;

    @Override
    public void postComment(Integer boardId, CommentRequest commentRequest) {
        AuthorityType authorityType = authenticationFacade.getAuthorityType();
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        boardRepository.findById(boardId)
                .orElseThrow(ApplicationNotFoundException::new);

        commentRepository.save(
                Comment.builder()
                    .content(commentRequest.getContent())
                    .createdAt(LocalDateTime.now())
                    .boardId(boardId)
                    .updateAt(LocalDateTime.now())
                    .authorType(authorityType)
                    .authorId(user.getId())
                    .build()
        );
    }

    @Override
    public void postSubComment(Integer commentId, CommentRequest commentRequest) {
        AuthorityType authorityType = authenticationFacade.getAuthorityType();
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        subCommentRepository.save(
                SubComment.builder()
                    .content(commentRequest.getContent())
                    .commentId(commentId)
                    .createdAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .authorType(authorityType)
                    .authorId(user.getId())
                    .build()
        );
    }

    @Override
    public void changeComment(Integer commentId, CommentRequest commentRequest) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!user.getType().equals(comment.getAuthorType()) || !user.getId().equals(comment.getAuthorId()))
            throw new PermissionDeniedException();

        commentRepository.save(comment.updateContent(commentRequest.getContent()));
    }

    @Override
    public void changeSubComment(Integer subCommentId, CommentRequest commentRequest) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        SubComment subComment = subCommentRepository.findById(subCommentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!user.getType().equals(subComment.getAuthorType()) || !user.getId().equals(subComment.getAuthorId()))
            throw new PermissionDeniedException();

        subCommentRepository.save(subComment.updateContent(commentRequest.getContent()));
    }

    @Override
    @Transactional
    public void deleteComment(Integer commentId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(RuntimeException::new);

        if (!user.getType().equals(comment.getAuthorType()) || !user.getId().equals(comment.getAuthorId()))
            throw new PermissionDeniedException();

        subCommentRepository.deleteAllByCommentId(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public void deleteSubComment(Integer subCommentId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        SubComment subComment = subCommentRepository.findById(subCommentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!user.getType().equals(subComment.getAuthorType()) || !user.getId().equals(subComment.getAuthorId()))
            throw new PermissionDeniedException();

        subCommentRepository.delete(subComment);
    }

}
