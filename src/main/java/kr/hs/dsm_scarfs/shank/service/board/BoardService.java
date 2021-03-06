package kr.hs.dsm_scarfs.shank.service.board;

import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.BoardContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
    ApplicationListResponse getBoardList(Integer classNumber, Pageable page);
    BoardContentResponse getBoardContent(Integer boardId);
    void deleteBoard(Integer boardId);
    Integer writeBoard(String title, String content, String classNumber, MultipartFile[] files);
    Integer changeBoard(Integer boardId, String title, String content, MultipartFile[] images);
    ApplicationListResponse searchBoard(String query, Integer classNumber, Pageable page);
}
