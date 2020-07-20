package kr.hs.dsm_scarfs.shank.entites.evaluation.mutual.repository;

import kr.hs.dsm_scarfs.shank.entites.evaluation.mutual.MutualEvaluation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MutualEvaluationRepository extends CrudRepository<MutualEvaluation, Integer> {
    Optional<MutualEvaluation> findByHomeworkIdAndUserIdAndTargetId(Integer homeworkId, Integer userId, Integer targetId);
    boolean existsByHomeworkIdAndUserIdAndTargetId(Integer homeworkId, Integer userId, Integer targetId);
}
