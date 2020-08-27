package kr.hs.dsm_scarfs.shank.entites.assignment.repository;

import kr.hs.dsm_scarfs.shank.entites.assignment.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignment, Integer> {

    Page<Assignment> findAllByDeadline1AfterOrderByCreatedAtDesc(Pageable page, LocalDate date);

    Page<Assignment> findAllByDeadline2AfterOrderByCreatedAtDesc(Pageable page, LocalDate date);

    Page<Assignment> findAllByDeadline3AfterOrderByCreatedAtDesc(Pageable page, LocalDate date);

    Page<Assignment> findAllByDeadline4AfterOrderByCreatedAtDesc(Pageable page, LocalDate date);

    Page<Assignment> findAllByTitleContainsOrDescriptionContainsOrderByCreatedAtDesc(String tileQuery, String descriptionQuery, Pageable page);

    Optional<Assignment> findTop1ByIdBeforeOrderByIdAsc(Integer id);

    Optional<Assignment> findTop1ByIdAfterOrderByIdAsc(Integer id);
}
