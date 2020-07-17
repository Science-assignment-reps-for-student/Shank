package kr.hs.dsm_scarfs.shank.entites.homework.repository;

import kr.hs.dsm_scarfs.shank.entites.homework.Homework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
@Repository
public interface HomeworkRepository extends CrudRepository<Homework, Integer> {

    Page<Homework> findAllByDeadline1After(Pageable page, LocalDate date);

    Page<Homework> findAllByDeadline2After(Pageable page, LocalDate date);

    Page<Homework> findAllByDeadline3After(Pageable page, LocalDate date);

    Page<Homework> findAllByDeadline4After(Pageable page, LocalDate date);

}
