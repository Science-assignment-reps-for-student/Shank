package kr.hs.dsm_scarfs.shank.entites.user.student.repository;

import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
    Optional<Student> findByEmail(String email);
    Optional<Student> findByStudentNumber(String studentNumber);
    List<Student> findAllByNameContainsOrStudentNumberContains(String query1, String query2);

    @Query(value = "select s.* from student as s join message m on m.student_id=s.id where m.admin_id=?1 group by s.id order by m.id", nativeQuery = true)
    List<Student> getChattedStudent(Integer adminId);
}
