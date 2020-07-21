package kr.hs.dsm_scarfs.shank.entites.user.student.repository;

import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {

    Optional<Student> findByEmail(String email);
    Optional<Student> findByStudentNumber(String studentNumber);

}
