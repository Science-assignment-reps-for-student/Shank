package kr.hs.dsm_scarfs.shank.entites.board.repository;

import kr.hs.dsm_scarfs.shank.entites.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends CrudRepository<Board, Integer> {
    Page<Board> findAllByClassNumberOrderByCreatedAtDesc(Integer classNumber, Pageable page);

    Optional<Board> findTop1ByIdBeforeAndClassNumber(Integer id, Integer classNumber);

    Optional<Board> findTop1ByIdAfterAndClassNumber(Integer id, Integer classNumber);
}
