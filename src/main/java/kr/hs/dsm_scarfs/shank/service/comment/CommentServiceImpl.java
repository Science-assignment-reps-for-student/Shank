package kr.hs.dsm_scarfs.shank.service.comment;

import kr.hs.dsm_scarfs.shank.entites.board.repository.BoardRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.Cocomment;
import kr.hs.dsm_scarfs.shank.entites.comment.Comment;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CocommentRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CommentRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
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
    public void postComment(Integer boardId, String content) {
        AuthorityType authorityType = authenticationFacade.getAuthorityType();
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        boardRepository.findById(boardId)
                .orElseThrow(RuntimeException::new);

        commentRepository.save(
                Comment.builder()
                    .content(content)
                    .createdAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .authorType(authorityType)
                    .authorId(user.getId())
                    .build()
        );
    }

    @Override
    public void postCocomment(Integer commentId, String content) {
        AuthorityType authorityType = authenticationFacade.getAuthorityType();
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        commentRepository.findById(commentId)
                .orElseThrow(RuntimeException::new);

        cocommentRepository.save(
                Cocomment.builder()
                    .content(content)
                    .createdAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .authorType(authorityType)
                    .authorId(user.getId())
                    .build()
        );
    }

    @Override
    public void changeComment(Integer commentId, String content) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(RuntimeException::new);

        if (!comment.getAuthorId().equals(user.getId()))
            throw new RuntimeException();

        commentRepository.save(comment.updateContent(content));
    }

    @Override
    public void changeCocomment(Integer cocommentId, String content) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Cocomment cocomment = cocommentRepository.findById(cocommentId)
                .orElseThrow(RuntimeException::new);

        if (!cocomment.getAuthorId().equals(user.getId()))
            throw new RuntimeException();

        cocommentRepository.save(cocomment.updateContent(content));
    }

    @Override
    public void deleteComment(Integer commentId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(RuntimeException::new);

        if (!comment.getAuthorId().equals(user.getId()))
            throw new RuntimeException();

        commentRepository.delete(comment);
    }

    @Override
    public void deleteCocomment(Integer cocommentId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Cocomment cocomment = cocommentRepository.findById(cocommentId)
                .orElseThrow(RuntimeException::new);

        if (!cocomment.getAuthorId().equals(user.getId()))
            throw new RuntimeException();

        cocommentRepository.delete(cocomment);
    }

}
