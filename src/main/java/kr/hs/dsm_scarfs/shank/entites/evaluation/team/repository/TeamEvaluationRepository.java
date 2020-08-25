package kr.hs.dsm_scarfs.shank.entites.evaluation.team.repository;

import kr.hs.dsm_scarfs.shank.entites.evaluation.team.TeamEvaluation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamEvaluationRepository extends CrudRepository<TeamEvaluation, Integer> {
    Optional<TeamEvaluation> findByAssignmentIdAndUserIdAndTargetId(Integer assignmentId, Integer userId, Integer targetId);
    boolean existsByAssignmentIdAndUserIdAndTargetId(Integer assignmentId, Integer userId, Integer targetId);
}
