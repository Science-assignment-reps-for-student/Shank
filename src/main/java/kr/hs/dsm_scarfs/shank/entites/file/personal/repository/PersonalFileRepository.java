package kr.hs.dsm_scarfs.shank.entites.file.personal.repository;

import kr.hs.dsm_scarfs.shank.entites.file.personal.PersonalFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalFileRepository extends CrudRepository<PersonalFile, Integer> {
    boolean existsByAssignmentIdAndStudentId(Integer assignment, Integer studentId);
}
