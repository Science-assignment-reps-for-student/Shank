package kr.hs.dsm_scarfs.shank.service.notice;

import kr.hs.dsm_scarfs.shank.payload.request.NoticeRequest;
import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.NoticeContentResponse;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    ApplicationListResponse getNoticeList(Pageable page);
    NoticeContentResponse getNoticeContent(Integer noticeId);
    Integer writeNotice(NoticeRequest noticeRequest);
    ApplicationListResponse searchNotice(String query, Pageable page);
}
