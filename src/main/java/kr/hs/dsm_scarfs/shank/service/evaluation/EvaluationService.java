package kr.hs.dsm_scarfs.shank.service.evaluation;

import kr.hs.dsm_scarfs.shank.payload.request.TeamEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.SelfEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.SelfEvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.TeamEvaluationInfo;

import java.util.List;

public interface EvaluationService {
    void personalEvaluation(SelfEvaluationRequest selfEvaluationRequest);
    void teamEvaluation(TeamEvaluationRequest teamEvaluationRequest);
    List<EvaluationResponse> getEvaluationInfo(Integer assignmentId);
    SelfEvaluationResponse personalEvaluationInfo(Integer assignmentId);
    TeamEvaluationInfo teamEvaluationInfo(Integer assignmentId, Integer targetId);
}
