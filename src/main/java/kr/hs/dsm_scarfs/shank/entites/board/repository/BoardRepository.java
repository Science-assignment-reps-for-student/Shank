package kr.hs.dsm_scarfs.shank.entites.board.repository;

import kr.hs.dsm_scarfs.shank.entites.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends CrudRepository<Board, Integer> {
    Page<Board> findAllByClassNumberAndTitleContainsOrClassNumberAndContentContainsOrderByCreatedAtDesc(
            Integer classNumber1, String title, Integer classNumber2, String content, Pageable pageable);

    Optional<Board> findTop1ByIdBeforeAndClassNumberOrderByIdDesc(Integer id, Integer classNumber);

    Optional<Board> findTop1ByIdAfterAndClassNumberOrderByIdAsc(Integer id, Integer classNumber);
}
