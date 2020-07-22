package kr.hs.dsm_scarfs.shank.service.notice;

import kr.hs.dsm_scarfs.shank.payload.response.NoticeContentResponse;
import kr.hs.dsm_scarfs.shank.payload.response.NoticeListResponse;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    NoticeListResponse getNoticeList(Pageable page);
    NoticeContentResponse getNoticeContent(Integer noticeId);
}
