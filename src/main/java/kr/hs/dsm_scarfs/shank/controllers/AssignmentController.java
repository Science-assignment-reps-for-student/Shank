package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.AssignmentContentResponse;
import kr.hs.dsm_scarfs.shank.service.assignment.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    public ApplicationListResponse assignmentList(Pageable page) {
        return assignmentService.getAssignmentList(page);
    }

    @GetMapping("/{assignmentId}")
    public AssignmentContentResponse getAssignmentContent(@PathVariable Integer assignmentId) {
        return assignmentService.getAssignmentContent(assignmentId);
    }
}
