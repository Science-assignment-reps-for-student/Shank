package kr.hs.dsm_scarfs.shank.service.board;

import kr.hs.dsm_scarfs.shank.entites.admin.Admin;
import kr.hs.dsm_scarfs.shank.entites.admin.repository.AdminRepository;
import kr.hs.dsm_scarfs.shank.entites.board.Board;
import kr.hs.dsm_scarfs.shank.entites.board.repository.BoardRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.Cocomment;
import kr.hs.dsm_scarfs.shank.entites.comment.Comment;
import kr.hs.dsm_scarfs.shank.entites.comment.enums.CommentEnum;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CocommentRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CommentRepository;
import kr.hs.dsm_scarfs.shank.entites.student.Student;
import kr.hs.dsm_scarfs.shank.entites.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.payload.response.*;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final CommentRepository commentRepository;
    private final CocommentRepository cocommentRepository;

    @Override
    public BoardListResponse getBoardList(Pageable page) {
        Page<Board> boardPage = boardRepository.findAllBy(page);

        List<BoardResponse> boardResponse = new ArrayList<>();

        for (Board board : boardPage) {
            Admin admin = adminRepository.findById(board.getAdminId())
                    .orElseThrow(RuntimeException::new);

            boardResponse.add(
                    BoardResponse.builder()
                        .boardId(board.getId())
                        .view(board.getView())
                        .title(board.getTitle())
                        .name(admin.getName())
                        .createdAt(board.getCreatedAt())
                        .build()
            );
        }

        return BoardListResponse.builder()
                    .totalElements((int) boardPage.getTotalElements())
                    .totalPages(boardPage.getTotalPages())
                    .boardResponses(boardResponse)
                    .build();
    }

    @Override
    public BoardContentResponse getBoardContent(Integer boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(RuntimeException::new);

        Admin admin = adminRepository.findById(board.getAdminId())
                .orElseThrow(RuntimeException::new);

        List<Comment> comment = commentRepository.findAllByBoardId(boardId);
        List<BoardCommentsResponse> commentsResponses = new ArrayList<>();

        for (Comment co : comment) {
            String commentWriterName;
            if (co.getAuthorType().equals(CommentEnum.ADMIN))
                commentWriterName = adminRepository.findById(co.getAuthorId())
                        .orElseThrow(RuntimeException::new).getName();
            else
                commentWriterName = studentRepository.findById(co.getAuthorId())
                    .orElseThrow(RuntimeException::new).getName();

            List<BoardCocommentsResponse> cocommentsResponses = new ArrayList<>();

            for (Cocomment coco : cocommentRepository.findAllByCommentId(co.getId())) {
                String cocomentWriterName;
                if (coco.getAuthorType().equals(CommentEnum.ADMIN))
                    cocomentWriterName = adminRepository.findById(co.getAuthorId())
                            .orElseThrow(RuntimeException::new).getName();
                else
                    cocomentWriterName = studentRepository.findById(co.getAuthorId())
                            .orElseThrow(RuntimeException::new).getName();

                cocommentsResponses.add(
                        BoardCocommentsResponse.builder()
                            .cocommentId(coco.getId())
                            .content(coco.getContent())
                            .createdAt(coco.getUpdateAt())
                            .writerName(cocomentWriterName)
                            .build()
                );
            }

            commentsResponses.add(
                BoardCommentsResponse.builder()
                        .commentId(co.getId())
                        .content(co.getContent())
                        .writerName(commentWriterName)
                        .createdAt(co.getUpdateAt())
                        .cocomments(cocommentsResponses)
                        .build()
            );
        }

        boardRepository.save(board.view());

        return BoardContentResponse.builder()
                    .title(board.getTitle())
                    .writerName(admin.getName())
                    .createdAt(board.getCreatedAt())
                    .view(board.getView())
                    .content(board.getContent())
                    .comments(commentsResponses)
                    .build();
    }

}
