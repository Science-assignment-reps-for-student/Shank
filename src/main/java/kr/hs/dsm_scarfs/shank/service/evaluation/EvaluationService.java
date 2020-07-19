package kr.hs.dsm_scarfs.shank.service.evaluation;

import kr.hs.dsm_scarfs.shank.payload.request.MutualEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.SelfEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;

import java.util.List;

public interface EvaluationService {
    void selfEvaluation(SelfEvaluationRequest selfEvaluationRequest);
    void mutualEvaluation(MutualEvaluationRequest mutualEvaluationRequest);
    List<EvaluationResponse> getEvaluationInfo(Integer homeworkId);
}
