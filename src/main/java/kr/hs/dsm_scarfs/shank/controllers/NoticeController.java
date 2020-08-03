package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.NoticeContentResponse;
import kr.hs.dsm_scarfs.shank.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ApplicationListResponse noticeList(Pageable page) {
        return noticeService.getNoticeList(page);
    }

    @GetMapping("/{noticeId}")
    public NoticeContentResponse getNoticeContent(@PathVariable Integer noticeId) {
        return noticeService.getNoticeContent(noticeId);
    }
}
