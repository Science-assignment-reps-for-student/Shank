package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.TeamEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.PersonalEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.SelfEvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.TargetEvaluationInfo;
import kr.hs.dsm_scarfs.shank.service.evaluation.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping("/info/{homeworkId}")
    public List<EvaluationResponse> getEvaluationInfo(@PathVariable Integer homeworkId) {
        return evaluationService.getEvaluationInfo(homeworkId);
    }

    @GetMapping("/info/personal/{homeworkId}")
    public SelfEvaluationResponse selfEvaluationInfo(@PathVariable Integer homeworkId) {
        return evaluationService.selfEvaluationInfo(homeworkId);
    }

    @GetMapping("/info/team/{homeworkId}")
    public TargetEvaluationInfo targetEvaluationInfo(@PathVariable Integer homeworkId, @RequestParam Integer targetId) {
        return evaluationService.targetEvaluationInfo(homeworkId, targetId);
    }

    @PostMapping("/self")
    public void selfEvaluation(@RequestBody PersonalEvaluationRequest selfEvaluationRequest) {
        evaluationService.selfEvaluation(selfEvaluationRequest);
    }

    @PostMapping("/mutual")
    public void mutualEvaluation(@RequestBody TeamEvaluationRequest teamEvaluationRequest) {
        evaluationService.mutualEvaluation(teamEvaluationRequest);
    }

}
