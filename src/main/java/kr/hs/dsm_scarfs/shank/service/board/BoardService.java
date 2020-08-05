package kr.hs.dsm_scarfs.shank.service.board;

import kr.hs.dsm_scarfs.shank.payload.request.BoardRequest;
import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.BoardContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
    ApplicationListResponse getBoardList(Pageable page);
    BoardContentResponse getBoardContent(Integer boardId);
    void deleteBoard(Integer boardId);
    void writeBoard(BoardRequest boardWrite, MultipartFile[] files);
    void changeBoard(Integer boardId, BoardRequest boardRequest, MultipartFile[] files);
}
