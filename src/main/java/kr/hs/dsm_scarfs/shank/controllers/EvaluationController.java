package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.TeamEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.PersonalEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.PersonalEvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.TeamEvaluationInfo;
import kr.hs.dsm_scarfs.shank.service.evaluation.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping("/info/{assignmentId}")
    public List<EvaluationResponse> getEvaluationInfo(@PathVariable Integer assignmentId) {
        return evaluationService.getEvaluationInfo(assignmentId);
    }

    @GetMapping("/info/personal/{assignmentId}")
    public PersonalEvaluationResponse selfEvaluationInfo(@PathVariable Integer assignmentId) {
        return evaluationService.personalEvaluationInfo(assignmentId);
    }

    @GetMapping("/info/team/{assignmentId}")
    public TeamEvaluationInfo targetEvaluationInfo(@PathVariable Integer assignmentId, @RequestParam Integer targetId) {
        return evaluationService.teamEvaluationInfo(assignmentId, targetId);
    }

    @PostMapping("/personal")
    public void selfEvaluation(@RequestBody PersonalEvaluationRequest selfEvaluationRequest) {
        evaluationService.personalEvaluation(selfEvaluationRequest);
    }

    @PostMapping("/team")
    public void mutualEvaluation(@RequestBody TeamEvaluationRequest teamEvaluationRequest) {
        evaluationService.teamEvaluation(teamEvaluationRequest);
    }

}
