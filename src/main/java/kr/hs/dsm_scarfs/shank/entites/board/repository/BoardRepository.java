package kr.hs.dsm_scarfs.shank.entites.board.repository;

import kr.hs.dsm_scarfs.shank.entites.board.Board;
import kr.hs.dsm_scarfs.shank.entites.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends CrudRepository<Board, Integer> {
    Page<Board> findAllBy(Pageable page);
    Page<Board> findAllByTitleContainsOrContentContains(String tileQuery, String contentQuery, Pageable page);

}
