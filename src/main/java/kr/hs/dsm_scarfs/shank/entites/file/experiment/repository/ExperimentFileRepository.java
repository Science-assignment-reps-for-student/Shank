package kr.hs.dsm_scarfs.shank.entites.file.experiment.repository;

import kr.hs.dsm_scarfs.shank.entites.file.experiment.ExperimentFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperimentFileRepository extends CrudRepository<ExperimentFile, Integer> {
    boolean existsByAssignmentIdAndStudentId(Integer assignment, Integer studentId);
}
