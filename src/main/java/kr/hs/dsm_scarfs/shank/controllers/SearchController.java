package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.service.assignment.AssignmentServiceImpl;
import kr.hs.dsm_scarfs.shank.service.search.SearchServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchServiceImpl searchService;

    @GetMapping("/{type}")
    public ApplicationListResponse noticeSearch(@PathVariable String type,
                                                @RequestParam("query") String query,
                                                Pageable page) {
        return searchService.searchApplication(type, query, page);
    }
}
