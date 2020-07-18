package kr.hs.dsm_scarfs.shank.entites.board.repository;

import kr.hs.dsm_scarfs.shank.entites.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CrudRepository<Board, Integer> {
    Page<Board> findAllBy(Pageable page);
}
