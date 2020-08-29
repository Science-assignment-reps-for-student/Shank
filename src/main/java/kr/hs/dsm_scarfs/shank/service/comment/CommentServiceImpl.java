package kr.hs.dsm_scarfs.shank.service.comment;

import kr.hs.dsm_scarfs.shank.entites.board.repository.BoardRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.Cocomment;
import kr.hs.dsm_scarfs.shank.entites.comment.Comment;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CocommentRepository;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final AuthenticationFacade authenticationFacade;
    private final UserFactory userFactory;

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final CocommentRepository cocommentRepository;

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
    public void postCocomment(Integer commentId, CommentRequest commentRequest) {
        AuthorityType authorityType = authenticationFacade.getAuthorityType();
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        cocommentRepository.save(
                Cocomment.builder()
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
    public void changeCocomment(Integer cocommentId, CommentRequest commentRequest) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Cocomment cocomment = cocommentRepository.findById(cocommentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!user.getType().equals(cocomment.getAuthorType()) || !user.getId().equals(cocomment.getAuthorId()))
            throw new PermissionDeniedException();

        cocommentRepository.save(cocomment.updateContent(commentRequest.getContent()));
    }

    @Override
    public void deleteComment(Integer commentId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(RuntimeException::new);

        if (!user.getType().equals(comment.getAuthorType()) || !user.getId().equals(comment.getAuthorId()))
            throw new PermissionDeniedException();

        cocommentRepository.deleteAllByCommentId(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public void deleteCocomment(Integer cocommentId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Cocomment cocomment = cocommentRepository.findById(cocommentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!user.getType().equals(cocomment.getAuthorType()) || !user.getId().equals(cocomment.getAuthorId()))
            throw new PermissionDeniedException();

        cocommentRepository.delete(cocomment);
    }

}
