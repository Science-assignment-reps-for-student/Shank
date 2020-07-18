package kr.hs.dsm_scarfs.shank.service.notice;

import kr.hs.dsm_scarfs.shank.entites.notice.Notice;
import kr.hs.dsm_scarfs.shank.entites.notice.repository.NoticeRepository;

import kr.hs.dsm_scarfs.shank.payload.response.NoticeListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.NoticeResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;

    @Override
    public NoticeListResponse getNoticeList(Pageable page) {
        Page<Notice> noticePage = noticeRepository.findAllBy(page);

        List<NoticeResponse> noticeResponses = new ArrayList<>();

        for (Notice notice : noticePage) {
            noticeResponses.add(
                    NoticeResponse.builder()
                    .noticeId(notice.getId())
                    .view(notice.getView())
                    .title(notice.getTitle())
                    .createdAt(notice.getCreatedAt())
                    .preViewContent(notice.getContent().substring(0, Math.min(150, notice.getContent().length())))
                    .build()
            );
        }


        return NoticeListResponse.builder()
                .totalElements((int) noticePage.getTotalElements())
                .totalPages(noticePage.getTotalPages())
                .noticeResponses(noticeResponses)
                .build();
    }

}
