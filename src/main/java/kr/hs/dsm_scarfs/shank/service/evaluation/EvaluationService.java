package kr.hs.dsm_scarfs.shank.service.evaluation;

import kr.hs.dsm_scarfs.shank.payload.request.TeamEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.PersonalEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.PersonalEvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.TeamEvaluationInfo;

import java.util.List;

public interface EvaluationService {
    void personalEvaluation(PersonalEvaluationRequest selfEvaluationRequest);
    void teamEvaluation(TeamEvaluationRequest teamEvaluationRequest);
    List<EvaluationResponse> getEvaluationInfo(Integer assignmentId);
    PersonalEvaluationResponse personalEvaluationInfo(Integer assignmentId);
    TeamEvaluationInfo teamEvaluationInfo(Integer assignmentId, Integer targetId);
}
