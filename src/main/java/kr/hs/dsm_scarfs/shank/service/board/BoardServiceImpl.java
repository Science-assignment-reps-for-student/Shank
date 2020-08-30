package kr.hs.dsm_scarfs.shank.service.board;

import kr.hs.dsm_scarfs.shank.entites.file.image.ImageFile;
import kr.hs.dsm_scarfs.shank.entites.file.image.repository.ImageFileRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.entites.user.admin.Admin;
import kr.hs.dsm_scarfs.shank.entites.user.admin.repository.AdminRepository;
import kr.hs.dsm_scarfs.shank.entites.board.Board;
import kr.hs.dsm_scarfs.shank.entites.board.repository.BoardRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.SubComment;
import kr.hs.dsm_scarfs.shank.entites.comment.Comment;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.SubCommentRepository;
import kr.hs.dsm_scarfs.shank.entites.comment.repository.CommentRepository;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.exceptions.ApplicationNotFoundException;
import kr.hs.dsm_scarfs.shank.exceptions.PermissionDeniedException;
import kr.hs.dsm_scarfs.shank.exceptions.UserNotFoundException;
import kr.hs.dsm_scarfs.shank.exceptions.UserNotLeaderException;
import kr.hs.dsm_scarfs.shank.payload.response.*;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import kr.hs.dsm_scarfs.shank.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final CommentService commentService;

    private final BoardRepository boardRepository;
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final CommentRepository commentRepository;
    private final ImageFileRepository imageFileRepository;
    private final SubCommentRepository subCommentRepository;

    private final AuthenticationFacade authenticationFacade;
    private final UserFactory userFactory;

    @Value("${image.upload.dir}")
    private String imageDirPath;

    @Override
    public ApplicationListResponse getBoardList(Integer classNumber, Pageable page) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());
        if (user.getType().equals(AuthorityType.ADMIN))
            return this.searchBoard("", classNumber, page);
        else
            return this.searchBoard("", user.getStudentClassNumber(), page);
    }

    @Override
    public BoardContentResponse getBoardContent(Integer boardId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Board board = boardRepository.findById(boardId)
                .orElseThrow(ApplicationNotFoundException::new);

        if (user.getType().equals(AuthorityType.STUDENT) &&
                !user.getStudentClassNumber().equals(board.getClassNumber()))
            throw new PermissionDeniedException();

        Admin admin = adminRepository.findById(board.getAdminId())
                .orElseThrow(UserNotLeaderException::new);

        List<Comment> comment = commentRepository.findAllByBoardId(boardId);
        List<BoardCommentsResponse> commentsResponses = new ArrayList<>();

        Board nextBoard = boardRepository.findTop1ByIdAfterAndClassNumberOrderByIdAsc(boardId, board.getClassNumber())
                .orElseGet(() -> Board.builder().build());

        Board preBoard = boardRepository.findTop1ByIdBeforeAndClassNumberOrderByIdDesc(boardId, board.getClassNumber())
                .orElseGet(() -> Board.builder().build());

        List<String> imageNames = new ArrayList<>();
        for (ImageFile imageFile : imageFileRepository.findByBoardId(boardId))
            imageNames.add(imageFile.getFileName());

        for (Comment co : comment) {
            User commentWriter;
            if (co.getAuthorType().equals(AuthorityType.ADMIN))
                commentWriter = adminRepository.findById(co.getAuthorId())
                        .orElseGet(() -> userFactory.getDefaultUser(Admin.class));
            else
                commentWriter = studentRepository.findById(co.getAuthorId())
                    .orElseGet(() -> userFactory.getDefaultUser(Student.class));

            List<BoardCocommentsResponse> cocommentsResponses = new ArrayList<>();
            for (SubComment coco : subCommentRepository.findAllByCommentId(co.getId())) {
                User cocommentWriter;
                if (coco.getAuthorType().equals(AuthorityType.ADMIN))
                    cocommentWriter = adminRepository.findById(co.getAuthorId())
                            .orElseGet(() -> userFactory.getDefaultUser(Admin.class));
                else
                    cocommentWriter = studentRepository.findById(co.getAuthorId())
                            .orElseGet(() -> userFactory.getDefaultUser(Student.class));

                cocommentsResponses.add(
                        BoardCocommentsResponse.builder()
                            .cocommentId(coco.getId())
                            .content(coco.getContent())
                            .createdAt(coco.getUpdateAt())
                            .studentNumber(cocommentWriter.getStudentNumber())
                            .writerName(cocommentWriter.getName())
                            .isMine(user.equals(cocommentWriter))
                            .build()
                );
            }

            commentsResponses.add(
                BoardCommentsResponse.builder()
                        .commentId(co.getId())
                        .content(co.getContent())
                        .studentNumber(commentWriter.getStudentNumber())
                        .writerName(commentWriter.getName())
                        .createdAt(co.getUpdateAt())
                        .isMine(user.equals(commentWriter))
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
                    .classNumber(board.getClassNumber())
                    .nextBoardTitle(nextBoard.getTitle())
                    .preBoardTitle(preBoard.getTitle())
                    .nextBoardId(nextBoard.getId())
                    .preBoardId(preBoard.getId())
                    .images(imageNames)
                    .comments(commentsResponses)
                    .build();
    }

    @Override
    @Transactional
    public void deleteBoard(Integer boardId) {
       boardRepository.findById(boardId)
                .orElseThrow(ApplicationNotFoundException::new);

        adminRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        for (Comment comment : commentRepository.findAllByBoardId(boardId))
            commentService.deleteComment(comment.getId());

        imageFileRepository.deleteByBoardId(boardId);
        boardRepository.deleteById(boardId);
    }

    @SneakyThrows
    @Override
    public Integer writeBoard(String title, String content, String classNumber, MultipartFile[] files) {
        Admin admin = adminRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(PermissionDeniedException::new);

        Board board = boardRepository.save(
                Board.builder()
                        .title(title)
                        .adminId(admin.getId())
                        .content(content)
                        .classNumber(Integer.parseInt(classNumber))
                        .createdAt(LocalDateTime.now())
                        .view(0)
                        .build()
        );

        for (MultipartFile file : Optional.ofNullable(files)
                .orElseGet(() -> new MultipartFile[0])) {
            String fileName = UUID.randomUUID().toString();
            imageFileRepository.save(
                    ImageFile.builder()
                            .boardId(board.getId())
                            .fileName(fileName)
                            .build()
            );
            file.transferTo(new File(imageDirPath, fileName));
        }

        return board.getId();
    }

    @SneakyThrows
    @Override
    public void changeBoard(Integer boardId, String title, String content, MultipartFile[] images) {
        adminRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(PermissionDeniedException::new);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(RuntimeException::new);

        boardRepository.save(board.update(title, content));

        List<ImageFile> imageFiles = imageFileRepository.findByBoardId(boardId);

        for (ImageFile imageFile : imageFiles) {
            new File(imageDirPath, imageFile.getFileName()).deleteOnExit();
        }

        imageFileRepository.deleteByBoardId(boardId);

        for (MultipartFile file : Optional.ofNullable(images)
                .orElseGet(() -> new MultipartFile[0])) {
            String fileName = UUID.randomUUID().toString();
            imageFileRepository.save(
                    ImageFile.builder()
                            .boardId(boardId)
                            .fileName(fileName)
                            .build()
            );

            file.transferTo(new File(imageDirPath, fileName));
        }

    }

    @Override
    public ApplicationListResponse searchBoard(String query, Integer classNumber, Pageable page) {
        page = PageRequest.of(Math.max(0, page.getPageNumber()-1), page.getPageSize());
        Page<Board> boardPage = boardRepository.findAllByClassNumberOrderByCreatedAtDesc(classNumber, page);

        List<BoardResponse> boardResponse = new ArrayList<>();

        for (Board board : boardPage) {
            Admin admin = adminRepository.findById(board.getAdminId())
                    .orElseGet(() -> userFactory.getDefaultUser(Admin.class));

            String preViewContent = board.getContent().substring(0, Math.min(50, board.getContent().length()));
            boardResponse.add(
                    BoardResponse.builder()
                            .boardId(board.getId())
                            .view(board.getView())
                            .title(board.getTitle())
                            .name(admin.getName())
                            .preViewContent(preViewContent)
                            .createdAt(board.getCreatedAt())
                            .build()
            );
        }

        return ApplicationListResponse.builder()
                .totalElements((int) boardPage.getTotalElements())
                .totalPages(boardPage.getTotalPages())
                .class_number(classNumber)
                .applicationResponses(boardResponse)
                .build();
    }

}
