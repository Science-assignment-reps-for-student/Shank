package kr.hs.dsm_scarfs.shank.service.evaluation;

import kr.hs.dsm_scarfs.shank.payload.request.TeamEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.PersonalEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.SelfEvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.TargetEvaluationInfo;

import java.util.List;

public interface EvaluationService {
    void selfEvaluation(PersonalEvaluationRequest selfEvaluationRequest);
    void mutualEvaluation(TeamEvaluationRequest teamEvaluationRequest);
    List<EvaluationResponse> getEvaluationInfo(Integer homeworkId);
    SelfEvaluationResponse selfEvaluationInfo(Integer homeworkId);
    TargetEvaluationInfo targetEvaluationInfo(Integer homeworkId, Integer targetId);
}
