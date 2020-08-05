package kr.hs.dsm_scarfs.shank.service.board;

import kr.hs.dsm_scarfs.shank.entites.file.image.Image;
import kr.hs.dsm_scarfs.shank.entites.file.image.repository.ImageRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.admin.Admin;
import kr.hs.dsm_scarfs.shank.entites.user.admin.repository.AdminRepository;
import kr.hs.dsm_scarfs.shank.entites.board.Board;
import kr.hs.dsm_scarfs.shank.entites.board.repository.BoardRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.Cocomment;
import kr.hs.dsm_scarfs.shank.entites.comment.Comment;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CocommentRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CommentRepository;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.exceptions.ApplicationNotFoundException;
import kr.hs.dsm_scarfs.shank.exceptions.PermissionDeniedException;
import kr.hs.dsm_scarfs.shank.exceptions.UserNotFoundException;
import kr.hs.dsm_scarfs.shank.exceptions.UserNotLeaderException;
import kr.hs.dsm_scarfs.shank.payload.request.BoardRequest;
import kr.hs.dsm_scarfs.shank.payload.response.*;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import kr.hs.dsm_scarfs.shank.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService, SearchService {

    private final BoardRepository boardRepository;
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final CocommentRepository cocommentRepository;
    private final AuthenticationFacade authenticationFacade;

    private final User defaultUser = Student.builder().name("(알수없음)").build();

    @Value("${image.upload.dir}")
    private String imageDirPath;

    @Override
    public ApplicationListResponse getBoardList(Pageable page) {
        return this.searchApplication("", page);
    }

    @Override
    public BoardContentResponse getBoardContent(Integer boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(ApplicationNotFoundException::new);

        Admin admin = adminRepository.findById(board.getAdminId())
                .orElseThrow(UserNotLeaderException::new);

        List<Comment> comment = commentRepository.findAllByBoardId(boardId);
        List<BoardCommentsResponse> commentsResponses = new ArrayList<>();

        Board nextBoard = boardRepository.findTop1ByIdAfterOrderByIdAsc(boardId)
                .orElseGet(() -> Board.builder().build());

        Board preBoard = boardRepository.findTop1ByIdBeforeOrderByIdAsc(boardId)
                .orElseGet(() -> Board.builder().build());

        for (Comment co : comment) {
            String commentWriterName;
            if (co.getAuthorType().equals(AuthorityType.ADMIN))
                commentWriterName = adminRepository.findById(co.getAuthorId())
                        .orElseGet(() -> (Admin) defaultUser).getName();
            else
                commentWriterName = studentRepository.findById(co.getAuthorId())
                    .orElseGet(() -> (Student) defaultUser).getName();

            List<BoardCocommentsResponse> cocommentsResponses = new ArrayList<>();

            for (Cocomment coco : cocommentRepository.findAllByCommentId(co.getId())) {
                String cocomentWriterName;
                if (coco.getAuthorType().equals(AuthorityType.ADMIN))
                    cocomentWriterName = adminRepository.findById(co.getAuthorId())
                            .orElseGet(() -> (Admin) defaultUser).getName();
                else
                    cocomentWriterName = studentRepository.findById(co.getAuthorId())
                            .orElseGet(() -> (Student) defaultUser).getName();

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
                    .nextBoardTitle(nextBoard.getTitle())
                    .preBoardTitle(preBoard.getTitle())
                    .nextBoardId(nextBoard.getId())
                    .preBoardId(preBoard.getId())
                    .comments(commentsResponses)
                    .build();
    }

    @Override
    public void deleteBoard(Integer boardId) {
        boardRepository.findById(boardId)
                .orElseThrow(ApplicationNotFoundException::new);

        adminRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        imageRepository.deleteById(boardId);
        boardRepository.deleteById(boardId);
    }

    @SneakyThrows
    @Override
    public void writeBoard(BoardRequest boardWrite, MultipartFile[] files) {
        Admin admin = adminRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(PermissionDeniedException::new);

        Board board = boardRepository.save(
                Board.builder()
                        .title(boardWrite.getTitle())
                        .adminId(admin.getId())
                        .content(boardWrite.getContent())
                        .createdAt(LocalDate.now())
                        .build()
        );

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID().toString();
            imageRepository.save(
                    Image.builder()
                        .boardId(board.getId())
                        .fileName(fileName)
                        .build()
            );
            file.transferTo(new File(imageDirPath, fileName));
        }


    }

    @SneakyThrows
    @Override
    public void changeBoard(Integer boardId, BoardRequest boardRequest, MultipartFile[] files) {
        Admin admin = adminRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(PermissionDeniedException::new);

        boardRepository.save(
                Board.builder()
                    .title(boardRequest.getTitle())
                    .content(boardRequest.getContent())
                    .createdAt(LocalDate.now())
                    .adminId(admin.getId())
                    .build()
        );

        List<Image> images = imageRepository.findByBoardId(boardId);
        
        for (Image image : images) {
            new File(imageDirPath, image.getFileName()).deleteOnExit();
        }

        imageRepository.deleteByBoardId(boardId);

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID().toString();
            imageRepository.save(
                    Image.builder()
                            .boardId(boardId)
                            .fileName(fileName)
                            .build()
            );

            file.transferTo(new File(imageDirPath, fileName));
        }

    }

    @Override
    public ApplicationListResponse searchApplication(String query, Pageable page) {
        Page<Board> boardPage = boardRepository.findAllBy(page);

        List<BoardResponse> boardResponse = new ArrayList<>();

        for (Board board : boardPage) {
            Admin admin = adminRepository.findById(board.getAdminId())
                    .orElseThrow(UserNotFoundException::new);

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
