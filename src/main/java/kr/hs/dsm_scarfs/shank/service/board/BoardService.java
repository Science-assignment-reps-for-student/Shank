package kr.hs.dsm_scarfs.shank.service.board;

import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.BoardContentResponse;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    ApplicationListResponse getBoardList(Pageable page);
    BoardContentResponse getBoardContent(Integer boardId);
}
