package kr.hs.dsm_scarfs.shank.service.board;

import kr.hs.dsm_scarfs.shank.entites.admin.Admin;
import kr.hs.dsm_scarfs.shank.entites.admin.repository.AdminRepository;
import kr.hs.dsm_scarfs.shank.entites.board.Board;
import kr.hs.dsm_scarfs.shank.entites.board.repository.BoardRepository;
import kr.hs.dsm_scarfs.shank.payload.response.BoardListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.BoardResponse;
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

}
