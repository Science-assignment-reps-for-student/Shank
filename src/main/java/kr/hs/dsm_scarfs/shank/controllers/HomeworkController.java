package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.response.HomeworkListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.HomeworkResponse;
import kr.hs.dsm_scarfs.shank.service.homework.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/homework")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;

    @GetMapping
    public HomeworkListResponse homeworkList(Pageable page) {
        return homeworkService.getHomeworkList(page);
    }
}
