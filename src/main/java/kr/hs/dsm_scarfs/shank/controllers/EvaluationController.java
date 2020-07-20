package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.MutualEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.SelfEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;
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

    @PostMapping("/self")
    public void selfEvaluation(@RequestBody SelfEvaluationRequest selfEvaluationRequest) {
        evaluationService.selfEvaluation(selfEvaluationRequest);
    }

    @PostMapping("/mutual")
    public void mutualEvaluation(@RequestBody MutualEvaluationRequest mutualEvaluationRequest) {
        evaluationService.mutualEvaluation(mutualEvaluationRequest);
    }

}
