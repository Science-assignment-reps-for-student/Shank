package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.service.homework.HomeworkServiceImpl;
import kr.hs.dsm_scarfs.shank.service.notice.NoticeService;
import kr.hs.dsm_scarfs.shank.service.notice.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final HomeworkServiceImpl noticeService;

    @GetMapping("/{type}")
    public ApplicationListResponse noticeSearch(@PathVariable String type, Pageable page) {
        return noticeService.searchApplication(type, page);
    }
}
