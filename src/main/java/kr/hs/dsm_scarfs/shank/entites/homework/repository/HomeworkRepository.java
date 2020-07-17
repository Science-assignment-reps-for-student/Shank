package kr.hs.dsm_scarfs.shank.entites.homework.repository;

import kr.hs.dsm_scarfs.shank.entites.homework.Homework;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends CrudRepository<Homework, Integer> {
    @Query(value = "SELECT * homework WHERE DATE(deadline_?1) > DATE(NOW())", nativeQuery = true)
    List<Homework> findHomework(String classNumber);
}
