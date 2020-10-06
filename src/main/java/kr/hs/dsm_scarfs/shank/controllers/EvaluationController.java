package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.MutualEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.SelfEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.SelfEvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.MutualEvaluationInfo;
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
    public SelfEvaluationResponse selfEvaluationInfo(@PathVariable Integer assignmentId) {
        return evaluationService.selfEvaluationInfo(assignmentId);
    }

    @GetMapping("/info/team/{assignmentId}")
    public MutualEvaluationInfo targetEvaluationInfo(@PathVariable Integer assignmentId, @RequestParam Integer targetId) {
        return evaluationService.mutualEvaluationInfo(assignmentId, targetId);
    }

    @PostMapping("/personal")
    public void selfEvaluation(@RequestBody SelfEvaluationRequest selfEvaluationRequest) {
        evaluationService.selfEvaluation(selfEvaluationRequest);
    }

    @PostMapping("/team")
    public void mutualEvaluation(@RequestBody MutualEvaluationRequest mutualEvaluationRequest) {
        evaluationService.mutualEvaluation(mutualEvaluationRequest);
    }

}
