package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.NoticeRequest;
import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.NoticeContentResponse;
import kr.hs.dsm_scarfs.shank.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ApplicationListResponse noticeList(Pageable page) {
        return noticeService.getNoticeList(page);
    }

    @PostMapping
    public Integer writeNotice(@RequestBody @Valid NoticeRequest noticeRequest) {
        return noticeService.writeNotice(noticeRequest);
    }

    @GetMapping("/{noticeId}")
    public NoticeContentResponse getNoticeContent(@PathVariable Integer noticeId) {
        return noticeService.getNoticeContent(noticeId);
    }

    @DeleteMapping("/{noticeId}")
    public void deleteNotice(@PathVariable Integer noticeId) {
        noticeService.deleteNotice(noticeId);
    }
}
