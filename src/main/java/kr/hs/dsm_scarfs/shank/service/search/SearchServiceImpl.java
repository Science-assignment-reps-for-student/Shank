package kr.hs.dsm_scarfs.shank.service.search;

import kr.hs.dsm_scarfs.shank.exceptions.InvalidSearchTypeException;
import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.service.assignment.AssignmentService;
import kr.hs.dsm_scarfs.shank.service.board.BoardService;
import kr.hs.dsm_scarfs.shank.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final AssignmentService assignmentService;
    private final BoardService boardService;
    private final NoticeService noticeService;

    @Override
    public ApplicationListResponse searchApplication(String type, String query, Pageable page) {
        switch (type) {
            case "assignment":
                return assignmentService.searchAssignment(query, page);
            case "board":
                return boardService.searchBoard(query, 1, page);
            case "notice":
                return noticeService.searchNotice(query, page);
            default:
                throw new InvalidSearchTypeException();
        }
    }

}
