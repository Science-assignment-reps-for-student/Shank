package kr.hs.dsm_scarfs.shank.service.evaluation;

import kr.hs.dsm_scarfs.shank.payload.request.MutualEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.SelfEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.SelfEvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.MutualEvaluationInfo;

import java.util.List;

public interface EvaluationService {
    void personalEvaluation(SelfEvaluationRequest selfEvaluationRequest);
    void teamEvaluation(MutualEvaluationRequest mutualEvaluationRequest);
    List<EvaluationResponse> getEvaluationInfo(Integer assignmentId);
    SelfEvaluationResponse personalEvaluationInfo(Integer assignmentId);
    MutualEvaluationInfo teamEvaluationInfo(Integer assignmentId, Integer targetId);
}
