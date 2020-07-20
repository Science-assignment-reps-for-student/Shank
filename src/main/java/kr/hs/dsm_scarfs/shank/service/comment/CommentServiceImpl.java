package kr.hs.dsm_scarfs.shank.service.comment;

import kr.hs.dsm_scarfs.shank.entites.board.repository.BoardRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.Comment;
import kr.hs.dsm_scarfs.shank.entites.comment.enums.CommentEnum;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CommentRepository;
import kr.hs.dsm_scarfs.shank.entites.student.Student;
import kr.hs.dsm_scarfs.shank.entites.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final AuthenticationFacade authenticationFacade;

    private final StudentRepository studentRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Override
    public void postComment(Integer boardId, String content) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(RuntimeException::new);

        boardRepository.findById(boardId)
                .orElseThrow(RuntimeException::new);

        commentRepository.save(
                Comment.builder()
                    .content(content)
                    .createdAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .authorType(CommentEnum.STUDENT)
                    .authorId(student.getId())
                    .build()
        );
    }
}
