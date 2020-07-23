package kr.hs.dsm_scarfs.shank.service.board;

import kr.hs.dsm_scarfs.shank.payload.response.BoardContentResponse;
import kr.hs.dsm_scarfs.shank.payload.response.BoardListResponse;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    BoardListResponse getBoardList(Pageable page);
    BoardContentResponse getBoardContent(Integer boardId);
}
