package kr.hs.dsm_scarfs.shank.service.board;

import kr.hs.dsm_scarfs.shank.entites.user.admin.Admin;
import kr.hs.dsm_scarfs.shank.entites.user.admin.repository.AdminRepository;
import kr.hs.dsm_scarfs.shank.entites.board.Board;
import kr.hs.dsm_scarfs.shank.entites.board.repository.BoardRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.Cocomment;
import kr.hs.dsm_scarfs.shank.entites.comment.Comment;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CocommentRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CommentRepository;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.payload.response.*;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import kr.hs.dsm_scarfs.shank.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService, SearchService {

    private final BoardRepository boardRepository;
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final CommentRepository commentRepository;
    private final CocommentRepository cocommentRepository;

    @Override
    public ApplicationListResponse getBoardList(Pageable page) {
        return this.searchApplication("", page);
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
            if (co.getAuthorType().equals(AuthorityType.ADMIN))
                commentWriterName = adminRepository.findById(co.getAuthorId())
                        .orElseThrow(RuntimeException::new).getName();
            else
                commentWriterName = studentRepository.findById(co.getAuthorId())
                    .orElseThrow(RuntimeException::new).getName();

            List<BoardCocommentsResponse> cocommentsResponses = new ArrayList<>();

            for (Cocomment coco : cocommentRepository.findAllByCommentId(co.getId())) {
                String cocomentWriterName;
                if (coco.getAuthorType().equals(AuthorityType.ADMIN))
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

    @Override
    public ApplicationListResponse searchApplication(String query, Pageable page) {
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

        return ApplicationListResponse.builder()
                .totalElements((int) boardPage.getTotalElements())
                .totalPages(boardPage.getTotalPages())
                .applicationResponses(boardResponse)
                .build();
    }

}
