package kr.hs.dsm_scarfs.shank.entites.evaluation.personal.repository;

import kr.hs.dsm_scarfs.shank.entites.evaluation.personal.PersonalEvaluation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalEvaluationRepository extends CrudRepository<PersonalEvaluation, Integer> {
    Optional<PersonalEvaluation> findByAssignmentIdAndStudentId(Integer assignmentId, Integer studentId);
    boolean existsByAssignmentIdAndStudentId(Integer assignmentId, Integer studentId);
}
